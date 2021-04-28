package top.bug1024.lovageshopping.common.util;

import com.sun.mail.util.MailSSLSocketFactory;
import lombok.SneakyThrows;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MessageUtil {

    public static String sendMsgByPhone(String phoneNumber,int n){
        String code = randomNumString(n);
        URL url = null;
        try {
            url = new URL("https://itdage.com/kkb/kkbsms?key=xzk&number="+phoneNumber+"&code="+code);
            //        获取返回信息
            URLConnection conn = url.openConnection();
            InputStream inputStream = conn.getInputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(inputStream,"UTF-8"));
            String text = br.readLine();
            br.close();
            return code;
        } catch (Exception e) {

        }

        return  null;
    }

    public static boolean checkPhone(String phoneNumber){
        String regex = "^(?:\\+?86)?1(?:3\\d{3}|5[^4\\D]\\d{2}|8\\d{3}|7(?:[01356789]\\d{2}|4(?:0\\d|1[0-2]|9\\d))|9[189]\\d{2}|6[567]\\d{2}|4[579]\\d{2})\\d{6}$";
        //正则表达式的模式 编译正则表达式
        Pattern p = Pattern.compile(regex);
        //正则表达式的匹配器
        Matcher m = p.matcher(phoneNumber);
        //进行正则匹配
        return m.matches();
    }

    public static String sendMsgByMail(String url,int n){
        String str = randomNumString(n);
        try {

            //设置发件人
            final String from = "77426372@qq.com";

            //设置收件人
            String to = url;

            //设置邮件发送的服务器，这里为QQ邮件服务器
            String host = "smtp.qq.com";

            //获取系统属性
            Properties properties = System.getProperties();

            //SSL加密
            MailSSLSocketFactory sf = new MailSSLSocketFactory();
            sf.setTrustAllHosts(true);
            properties.put("mail.smtp.ssl.enable", "true");
            properties.put("mail.smtp.ssl.socketFactory", sf);

            //设置系统属性
            properties.setProperty("mail.smtp.host", host);
            properties.put("mail.smtp.auth", "true");

            //获取发送邮件会话、获取第三方登录授权码
            Session session = Session.getDefaultInstance(properties, new Authenticator() {
                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(from, "obvzoeksjysabich");//etzgjqjpqftsbgid

                }
            });

            Message message = new MimeMessage(session);

            //防止邮件被当然垃圾邮件处理，披上Outlook的马甲
            message.addHeader("X-Mailer","Microsoft Outlook Express 6.00.2900.2869");

            message.setFrom(new InternetAddress(from));

            message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));

            //邮件标题
            message.setSubject("验证码");

            BodyPart bodyPart = new MimeBodyPart();

            bodyPart.setText("请输入以下"+n+"位验证码\n验证码为："+str);

            Multipart multipart = new MimeMultipart();

            multipart.addBodyPart(bodyPart);

            //附件
//            bodyPart = new MimeBodyPart();
//            String fileName = "文件路径";
//            DataSource dataSource = new FileDataSource(fileName);
//            bodyPart.setDataHandler(new DataHandler(dataSource));
//            bodyPart.setFileName("文件显示的名称");
//            multipart.addBodyPart(bodyPart);
//
            message.setContent(multipart);

            Transport.send(message);
            System.out.println("mail transports successfully");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return str;


    }

    private static String randomNumString(int n){
        String str = "";
        int i=0;
        for (;i<n;i++){
            str+= ((int)(Math.random()*10));
        }
        return str;
    }

    public static boolean checkEmaile(String emaile){
        /**
         *   正则表达式实用规则
         *   ^匹配输入字符串的开始位置
         *   $结束的位置
         *   \转义字符 eg:\. 匹配一个. 字符  不是任意字符 ，转义之后让他失去原有的功能
         *   \t制表符
         *   \n换行符
         *   \\w匹配字符串  eg:\w不能匹配 因为转义了
         *   \w匹配包括字母数字下划线的任何单词字符
         *   \s包括空格制表符换行符
         *   *匹配前面的子表达式任意次
         *   .小数点可以匹配任意字符
         *   +表达式至少出现一次
         *   ?表达式0次或者1次
         *   {10}重复10次
         *   {1,3}至少1-3次
         *   {0,5}最多5次
         *   {0,}至少0次 不出现或者出现任意次都可以 可以用*号代替
         *   {1,}至少1次  一般用+来代替
         *   []自定义集合     eg:[abcd]  abcd集合里任意字符
         *   [^abc]取非 除abc以外的任意字符
         *   |  将两个匹配条件进行逻辑“或”（Or）运算
         *   [1-9] 1到9 省略123456789
         *    邮箱匹配 eg: ^[a-zA-Z_]{1,}[0-9]{0,}@(([a-zA-z0-9]-*){1,}\.){1,3}[a-zA-z\-]{1,}$
         *
         */
        String RULE_EMAIL = "^\\w+((-\\w+)|(\\.\\w+))*\\@[A-Za-z0-9]+((\\.|-)[A-Za-z0-9]+)*\\.[A-Za-z0-9]+$";
        //正则表达式的模式 编译正则表达式
        Pattern p = Pattern.compile(RULE_EMAIL);
        //正则表达式的匹配器
        Matcher m = p.matcher(emaile);
        //进行正则匹配
        return m.matches();
    }


}
