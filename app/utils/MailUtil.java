package utils;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Date;
import java.util.Properties;

/**
 * Created by harvey on 15-9-2.
 *
 * Utils to send email
 */
public class MailUtil {
    private static final String   HOST                = "smtp.163.com";
    private static final String   PROTOCOL     = "stmp";
    private static final int        PORT                 = 25;
    private static final String   FROM                = "socialplus@163.com";
    private static final String   PWD                  = "wgaobhpkjbuujrta";

    private static Session getSession(){
        Properties props = new Properties();
        props.put("mail.smtp.host",HOST);
        props.put("mail.store.protocol",PROTOCOL);
        props.put("mail.smtp.port",PORT);
        props.put("mail.smtp.auth",true);

        Authenticator authenticator = new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(FROM,PWD);
            }
        };
        Session session = Session.getInstance(props,authenticator);
        return session;
    }

    public static boolean send(String toEmail ,String subject, String content)  {
        Session session = getSession();
        try {
            //System.out.println("--send--" + content);
            // Instantiate a message
            Message msg = new MimeMessage(session);

            //Set message attributes
            msg.setFrom(new InternetAddress(FROM));
            InternetAddress[] address = {new InternetAddress(toEmail)};
            msg.setRecipients(Message.RecipientType.TO, address);
            msg.setSubject(subject);
            msg.setSentDate(new Date());
            msg.setContent(content, "text/html;charset=utf-8");

            //Send the message
            Transport.send(msg);
            return true;
        }
        catch (MessagingException mex) {
            mex.printStackTrace();
            return false;
        }
    }
}
