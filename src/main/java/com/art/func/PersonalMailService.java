package com.art.func;

import com.art.config.AppSecurityConfig;
import com.art.model.AppUser;
import com.art.model.supporting.GenericResponse;
import com.art.model.supporting.SendingMail;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.web.multipart.MultipartFile;

import javax.mail.Authenticator;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.internet.InternetAddress;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Objects;
import java.util.Properties;

@org.springframework.stereotype.Service
public class PersonalMailService {
    private static final String ENCODING = StandardCharsets.UTF_8.name();

    public GenericResponse sendEmails(AppUser user, SendingMail sendingMail, String username, String pwd,
                                      String who, List<MultipartFile> file) {
        GenericResponse response = new GenericResponse();
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();

        String fileName = "mail.ru.properties";
        Properties prop = new Properties();
        InputStream input;
        try {
            input = AppSecurityConfig.class.getClassLoader().getResourceAsStream(fileName);
            prop.load(input);
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        //Using mail.ru
        mailSender.setHost(prop.getProperty("mail.host"));
        mailSender.setPort(Integer.parseInt(prop.getProperty("mail.port")));

        mailSender.setUsername(username);
        mailSender.setPassword(pwd);
        mailSender.setProtocol(prop.getProperty("mail.protocol"));

        Authenticator auth = new MyAuthenticator(mailSender.getUsername(), mailSender.getPassword());

        System.setProperty("mail.mime.encodefilename", "true");
        System.setProperty("mail.mime.encodeparameters", "false");
        Properties javaMailProperties = new Properties();

        javaMailProperties.put("mail.transport.protocol", prop.getProperty("mail.protocol"));
        javaMailProperties.put("mail.smtp.port", Integer.parseInt(prop.getProperty("mail.port")));
        javaMailProperties.put("mail.smtp.host", prop.getProperty("mail.host"));
        javaMailProperties.put("mail.smtp.auth", prop.getProperty("mail.smtp.auth"));
        javaMailProperties.put("mail.debug", prop.getProperty("mail.debug"));
        javaMailProperties.put("mail.smtp.starttls.enable", prop.getProperty("mail.smtp.starttls.enable"));
        javaMailProperties.put("mail.mime.charset", ENCODING);

        Session session = Session.getInstance(javaMailProperties, auth);

        mailSender.setJavaMailProperties(javaMailProperties);
        mailSender.setSession(session);
        final String[] attachName = {""};
        mailSender.send(mimeMessage -> {
            MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage, true, ENCODING);
            messageHelper.setTo(user.getProfile().getEmail());
            messageHelper.setSubject(sendingMail.getSubject());
            messageHelper.setText(sendingMail.getBody(), true);
            messageHelper.setFrom(new InternetAddress(username, who));
            if (!Objects.equals(file, null)) {
                file.forEach(f -> {
                    attachName[0] = f.getOriginalFilename();

                    if (!attachName[0].equals("")) {
                        try {
                            messageHelper.addAttachment(attachName[0], f);
                            response.setMessage("Письма успешно отправлены.");
                        } catch (MessagingException e) {
                            response.setError("При отправке писем что-то пошло не так.");
                        }
                    }
                });
            }
            response.setMessage("Письма успешно отправлены.");
        });
        return response;
    }

    class MyAuthenticator extends Authenticator {
        private String user;
        private String password;

        MyAuthenticator(String user, String password) {
            this.user = user;
            this.password = password;
        }

        public PasswordAuthentication getPasswordAuthentication() {
            String user = this.user;
            String password = this.password;
            return new PasswordAuthentication(user, password);
        }

    }

}
