package model;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

public class JavaMailUtil {
    public static void sendMail(String recipient){
        try {
            Properties properties = new Properties();
            properties.put("mail.smtp.auth", "true");
            properties.put("mail.smtp.starttls.enable", "true");
            properties.put("mail.smtp.host", "smtp.gmail.com");
            properties.put("mail.smtp.port", "587");

            String myEmailAccount = "mafara.ngoni@gmail.com";
            String myAccountPassword = "doavwrikfeuoniag";
            Session session = Session.getInstance(properties, new Authenticator() {
                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(myEmailAccount, myAccountPassword);
                }
            });
            Message message = prepareMessage(session, myEmailAccount, recipient);
            assert message != null;
            Transport.send(message);
            System.out.println("Message sent successfully!");
        }catch (MessagingException ex){
            Logger.getLogger(JavaMailUtil.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    private static Message prepareMessage(Session session, String myEmailAccount, String recipient){
        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(myEmailAccount));
            message.setRecipient(Message.RecipientType.TO, new InternetAddress(recipient));
            message.setSubject("Account Verification");
            String htmlCode = "<h1>Welcome to Mafara Dairy!</h1> <br>" +
                    "<h2>Your account has been successfully created. Enjoy our services.</h2></br>"
                    + "<br><p>Best Regards</p><p>Ngonidzashe Mafara</p></br>";
            message.setContent(htmlCode,"text/html");
            return message;
        }catch (MessagingException ex){
            Logger.getLogger(JavaMailUtil.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
}
