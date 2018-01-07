package com.art.model.supporting;

import com.art.model.Users;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.stereotype.Service;

import javax.mail.Message;
import javax.mail.internet.InternetAddress;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

@Service("mailService")
public class MailService {

    @Autowired
    private
    JavaMailSender mailSender;

    private Properties prop = new Properties();

    public void sendEmail(Object object, String body, String subject) {
        String fileName = "mail.ru.properties";

        InputStream input;
        try{
            input = MailService.class.getClassLoader().getResourceAsStream(fileName);
            prop.load(input);
        }catch (IOException ex){
            ex.printStackTrace();
        }

        Users users = (Users) object;

        MimeMessagePreparator preparator = getMessagePreparator(users, body, subject);
        try {
            mailSender.send(preparator);
        } catch (MailException ex) {
            System.err.println(ex.getMessage());
        }
    }

    private MimeMessagePreparator getMessagePreparator(final Users user, String body, String subject) {

            return mimeMessage -> {
            mimeMessage.setFrom(new InternetAddress(prop.getProperty("mail.username"),
                    "Восстановление пароля"));
            mimeMessage.setRecipient(Message.RecipientType.TO,
                    new InternetAddress(user.getEmail()));
            mimeMessage.setText(body, "UTF-8", "HTML");
            mimeMessage.setSubject(subject);
        };
    }
}
