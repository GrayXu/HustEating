package com.grayxu.husteating.UI;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.grayxu.husteating.R;
import com.grayxu.husteating.background.Mail;

import java.io.UnsupportedEncodingException;

import javax.mail.MessagingException;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
    }

    @Override
    public void onClick(View view) {
        String name = String.valueOf(((EditText) findViewById(R.id.ETname)).getText());
        String email = String.valueOf(((EditText) findViewById(R.id.ETemail)).getText());
        int idNow = view.getId();
        if(idNow == R.id.butRegister){
            register(name, email);
        }else if (idNow == R.id.butMailCode){
            sendCode(name, email);
        }
    }

    private void register(String name, String email){
        //TODO:向服务器发送请求交互，以下为成功逻辑
        SharedPreferences sp = getSharedPreferences("MainActivity", MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("name", name);
        editor.putString("email", email);
        editor.apply();
    }

    private void sendCode(String name, String email){
        //TODO:向服务器确定没有这个邮箱的注册记录

        Mail mail = new Mail(name, email, getString(R.string.mail_password));
        try {
            mail.sendMail();
        } catch (MessagingException e) {
            Log.d("RegisterAc onClick", "本地问题");
            Toast.makeText(this, "好像有点问题，不然试试重启？", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            Log.d("RegisterAc onClick", "网络问题");
            Toast.makeText(this, "好像有点问题，检查下网络设置？", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }
}
