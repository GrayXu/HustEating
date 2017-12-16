package com.grayxu.husteating.background;

/**
 * Created by Administrator on 2017/12/13.
 */
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.Properties;

public class Mail {

    //这个公邮的授权码应该放到String资源包内或者做混淆
    final private String myEmailAccount = "husteating@126.com";
    private String myEmailPassword;
    final private String myEmailSMTPHost = "smtp.126.com";//暂时使用126邮箱作为公邮

    private String name;
    private String email;

    public Mail(String name, String email, String password){
        this.name = name;
        this.email = email;
        this.myEmailPassword = password;
    }

    /**
     * 发送邮件，从公邮里发邮件给成员变量email。
     * @throws Exception 可能会有异常抛出，建议打出Log。
     */
    public void sendMail() throws MessagingException, UnsupportedEncodingException {
        //创建参数配置, 用于连接邮件服务器的参数配置
        Properties props = new Properties();                    // 参数配置
        props.setProperty("mail.transport.protocol", "smtp");   // 使用的协议（JavaMail规范要求）
        props.setProperty("mail.smtp.host", myEmailSMTPHost);   // 发件人的邮箱的 SMTP 服务器地址
        props.setProperty("mail.smtp.auth", "true");            // 需要请求认证

        //根据配置创建会话对象, 用于和邮件服务器交互
        Session session = Session.getInstance(props);
        session.setDebug(true);                                 // 设置为debug模式, 可以查看详细的发送 log

        // 3. 创建一封邮件
        MimeMessage message = createMimeMessage(session, myEmailAccount, email);

        Transport transport = session.getTransport();

        // 5. 使用 邮箱账号 和 密码 连接邮件服务器, 这里认证的邮箱必须与 message 中的发件人邮箱一致, 否则报错
        transport.connect(myEmailAccount, myEmailPassword);
        transport.sendMessage(message, message.getAllRecipients());
        transport.close();

    }

    /**
     * 创建一封邮件
     *
     * @param session 和服务器交互的会话
     * @param sendMail 发件人邮箱
     * @param receiveMail 收件人邮箱
     * @return
     * @throws Exception
     */
    private MimeMessage createMimeMessage(Session session, String sendMail, String receiveMail) throws MessagingException, UnsupportedEncodingException {
        MimeMessage message = new MimeMessage(session);

        message.setFrom(new InternetAddress(sendMail, "吃在华科", "UTF-8"));

        message.setRecipient(MimeMessage.RecipientType.TO, new InternetAddress(receiveMail, this.name, "UTF-8"));

        message.setSubject("吃在华科邮件注册验证码", "UTF-8");
        String content = this.name+"，你好, 您的验证码如下<br/>"+getCode()+"<p> 您不需要回复这封邮件。<p/>";
        message.setContent(content, "text/html;charset=UTF-8");
        message.setSentDate(new Date());
        message.saveChanges();

        return message;
    }

//    public static void main(String[] args){
//        Mail mail = new Mail("Gray","285293728@qq.com");
//        try {
//            mail.sendMail();
//        } catch (MessagingException e) {
//            e.printStackTrace();
//        } catch (UnsupportedEncodingException e) {
//            e.printStackTrace();
//        }
//
//    }

    /**
     * 获取由邮箱名计算获得的验证码
     * @return 一个六位数的验证码
     */
    private String getCode() {

        int hashcode = email.hashCode();//s[0]*31^(n-1) + s[1]*31^(n-2) + ... + s[n-1]
        hashcode = Math.abs(hashcode) * 410 % 1000000;

        while (hashcode < 100000){
            hashcode = hashcode * 6;
        }

        return String.valueOf(hashcode);
    }

}