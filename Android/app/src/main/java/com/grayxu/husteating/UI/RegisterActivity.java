package com.grayxu.husteating.UI;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.grayxu.husteating.R;
import com.grayxu.husteating.background.Client;
import com.grayxu.husteating.background.JsonUtil;
import com.grayxu.husteating.background.Mail;
import com.grayxu.husteating.background.UserStatus;

import java.io.UnsupportedEncodingException;

import javax.mail.MessagingException;

/**
 * 注册逻辑的活动
 */
public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {

    /**
     * msg what： 1：检查注册界面输入信息是否正确。1 2 3 4 5分别指用户名、密码、二次确定密码、邮箱、验证码
     */
    @SuppressLint("HandlerLeak")//不能持有外部引用，全部做局部变量
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            Log.d("handleMessage", "收到消息 what为" + msg.what);
            if (msg.what == 1) {
                View focusView = null;
                Log.d("handleMessage", "msg.arg1为" + msg.arg1);
                if (msg.arg2 == 0) {
                    switch (msg.arg1) {
                        case 1:
                            focusView = etName;
                            etName.setError("名字太短了");
                            break;
                        case 2:
                            focusView = etPassword;
                            etPassword.setError("密码太短了");
                            break;
                        case 3:
                            focusView = etConfirmPassword;
                            etConfirmPassword.setError("两次输入的密码不同");
                            break;
                        case 4:
                            focusView = etEmail;
                            etEmail.setError("邮箱不合法");
                            break;
                        case 5:
                            focusView = etCheckCode;
                            etCheckCode.setError("验证码错误");
                            break;
                    }
                    focusView.requestFocus();
                }
                if (msg.arg2 == 1) {//反馈出错信息
                    Toast.makeText(RegisterActivity.this, "邮件发送失败，请检查网络", Toast.LENGTH_SHORT).show();
                } else if (msg.arg2 == 2) {
                    //*此处进行的SP保存屡屡发生错误，故用一个外部单例类来解决*
                    UserStatus userStatus = UserStatus.getUserStatus();
                    userStatus.setEmail(email);
                    userStatus.setName(name);
                    userStatus.setLogin(true);
                    userStatus.setMajor(major);
                    userStatus.setProvince(province);
                    userStatus.setSex(sex);
//                    userStatus.setMajor();
                    Log.d("注册成功，进行提交结果", "name " + name + " email " + email);
                    ActivityManager.getActivityManager().finishLoginRegister();
                } else if(msg.arg2 == 3){
                    //得到的结果是不重复则发送邮件
                    String name = UserStatus.getUserStatus().getName();
                    String email = UserStatus.getUserStatus().getEmail();
                    Toast.makeText(RegisterActivity.this,"收到了回来的Json",Toast.LENGTH_SHORT);
                    if (client.getBoolReply()){
                        sendCode(name, email);
                    }else {
                        Toast.makeText(RegisterActivity.this,"邮件重复",Toast.LENGTH_SHORT);
                    }
                }

            }

        }
    };

    private EditText etName;
    private EditText etEmail;
    private EditText etPassword;
    private EditText etConfirmPassword;
    private EditText etCheckCode;
    private Spinner spProvince;
    private Spinner spMajor;
    private RadioGroup radioGroup;

    private String name;
    private String email;
    private String province;
    private String major;
    private String sex;

    private Client client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityManager.getActivityManager().addActivity("RegisterActivity", this);
        setContentView(R.layout.activity_register);

        etName = (EditText) findViewById(R.id.ETname);
        etEmail = (EditText) findViewById(R.id.ETemail);
        spMajor = (Spinner) findViewById(R.id.spiMajor);
        spProvince = (Spinner) findViewById(R.id.spiProvince);

        etPassword = (EditText) findViewById(R.id.ETpassword);
        etConfirmPassword = (EditText) findViewById(R.id.ETconfirmPassword);
        etCheckCode = (EditText) findViewById(R.id.ETCheckCode);
        radioGroup = (RadioGroup) findViewById(R.id.rgGender);


        findViewById(R.id.butRegister).setOnClickListener(this);
        findViewById(R.id.butMailCode).setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        name = String.valueOf(etName.getText());
        String email = String.valueOf(etEmail.getText());
        int idNow = view.getId();
        if (idNow == R.id.butRegister) {
            CheckInfoThread checkInfoThread = new CheckInfoThread();
            checkInfoThread.start();

        } else if (idNow == R.id.butMailCode) {
            if (!isEmailValid(email)) {
                Toast.makeText(this, "邮箱格式不合法", Toast.LENGTH_SHORT).show();
            } else {
                checkEmail();
                Toast.makeText(this, "发送邮件中...", Toast.LENGTH_SHORT).show();
            }
        }
    }

    /**
     * 检查邮箱是否重复，若不则发送邮件
     */
    private void checkEmail(){
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                SharedPreferences sp = getSharedPreferences("MainActivity",MODE_PRIVATE);
//                String IP = sp.getString("IP", null);
//                String port = sp.getString("port", null);
//                Log.d("checkEmail", "IP:"+IP+" port:"+port);
//                if (IP == null || port == null){
//                    Log.e("sendCode","SP又狗日出问题了");
//                }else {
//                    client = new Client(IP,Integer.parseInt(port),new JsonUtil().getEmailAskJson(email),handler);
//                    new Thread(new Runnable() {
//                        @Override
//                        public void run() {
//                            client.exchangeMessage();
//                            try {
//                                Thread.sleep(2000);
//                            } catch (InterruptedException e) {
//                                e.printStackTrace();
//                            }
//                            client.sendMessage();
//                        }
//                    }).start();
//
//                }
//            }
//        }).start();
        client.sendMessage();
    }

    /**
     * 向该邮箱发送邮件
     *
     * @param name  用户名
     * @param email 用户邮箱
     */
    private void sendCode(String name, final String email) {

        final Mail mail = new Mail(name, email, getString(R.string.mail_password));
        Log.d("sendCode", "尝试发送邮件");

        new Thread(new Runnable() {//异步发送验证码邮件
            @Override
            public void run() {
                try {
                    mail.sendCodeMail();
                } catch (MessagingException e) {
                    Log.d("sendCode", "邮件发送失败");
                    Message message = new Message();
                    message.what = 1;
                    message.arg2 = 2;
                    handler.sendMessage(message);
                    e.printStackTrace();
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }
        }).start();

    }

    /**
     * Email地址必须含有 @ 与 . 两个字符
     *
     * @param email
     * @return 合法与否
     */
    private boolean isEmailValid(String email) {
        if (email.contains("@")) {
            if (email.contains(".")) {
                return true;
            }
        }
        return false;
    }

    /**
     * 密码是否合法
     *
     * @param password
     * @return
     */
    private boolean isPasswordValid(String password) {
        return password.length() >= 6;//密码需要大于六位
    }

    /**
     * 验证码是否合法（即是否为六位数字）
     *
     * @param checkCode
     * @return
     */
    private boolean isCheckCodeValid(String checkCode) {
        return checkCode.length() == 6;
    }

    /**
     * 检查用户名长度长度是否大于等于6
     *
     * @param name
     * @return
     */
    private boolean isNameValid(String name) {
        return name.length() >= 6;
    }

    /**
     * 检查前后两次输入的密码是否相同
     *
     * @param password
     * @param confirmPassword
     * @return
     */
    private boolean isPasswordConfirmed(String password, String confirmPassword) {
        return password.equals(confirmPassword);
    }

    /**
     * 负责检查与确定成功注册的线程
     */
    class CheckInfoThread extends Thread {
        @Override
        public void run() {
            Log.d("CheckInfoThread run", "检查线程开始运行");
            name = etName.getText().toString();
            email = etEmail.getText().toString().toLowerCase();
            major = spMajor.getSelectedItem().toString();
            province = spProvince.getSelectedItem().toString();
            if (radioGroup.getCheckedRadioButtonId() == R.id.rbMan){
                sex = "男";
            }else if(radioGroup.getCheckedRadioButtonId() == R.id.rbWoman){
                sex = "女";
            }

            String password = etPassword.getText().toString();
            String confirmPassword = etConfirmPassword.getText().toString();
            String checkCode = etCheckCode.getText().toString();
            int genderButtonID = radioGroup.getCheckedRadioButtonId();
            boolean cancel = false;//外部flag

            Message message = new Message();
            message.what = 1;
            if (TextUtils.isEmpty(name) || !isNameValid(name)) {
                message.arg1 = 1;
                cancel = true;
            } else if (!isPasswordValid(password)) {
                message.arg1 = 2;
                cancel = true;
            } else if (!isPasswordConfirmed(password, confirmPassword)) {
                message.arg1 = 3;
                cancel = true;
            } else if (!isEmailValid(email)) {
                message.arg1 = 4;
                cancel = true;
            } else if (!isCheckCodeValid(checkCode)) {
                message.arg1 = 5;
                cancel = true;
            } else if (genderButtonID != R.id.rbMan && genderButtonID != R.id.rbWoman) {
                Toast.makeText(RegisterActivity.this, "性别未填", Toast.LENGTH_SHORT).show();
                cancel = true;
            }

            if (cancel) {
                handler.sendMessage(message);
            } else {//填写信息格式无误，进入保存逻辑
                if (checkCode.equals(getCode(email))) {
                    Log.d("CheckInfoThread", "检查完毕，所有信息正确");
                    message.arg2 = 2;
                    handler.sendMessage(message);
                }
            }

        }

        /**
         * 获取由邮箱名计算获得的验证码
         *
         * @return 一个六位数的验证码
         */
        private String getCode(String email) {

            int hashcode = email.hashCode();//s[0]*31^(n-1) + s[1]*31^(n-2) + ... + s[n-1]
            hashcode = Math.abs(hashcode) * 410 % 1000000;

            while (hashcode < 100000) {
                hashcode = hashcode * 6;
            }

            return String.valueOf(hashcode);
        }
    }
}
