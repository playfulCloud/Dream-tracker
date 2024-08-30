package com.dreamtracker.app.infrastructure.mail;


import com.dreamtracker.app.user.config.CurrentUserProvider;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

@Service
@RequiredArgsConstructor
public class MailService {

    private final JavaMailSender javaMailSender;
    private final ResourceLoader resourceLoader;


    public boolean sendPasswordResetMail(String email,String resetToken, String name){
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        try {
            mimeMessage.setFrom("dontreplydreamtracker@outlook.com");
            mimeMessage.setRecipients(MimeMessage.RecipientType.TO, InternetAddress.parse(email));
            mimeMessage.setSubject("Password reset");
            String content = readFileContent("PasswordReset.html");
            content = content.replace("${name}", name);
            content = content.replace("${resetToken}", resetToken);

            mimeMessage.setContent(content, "text/html");
            System.out.println(mimeMessage);
        } catch (MessagingException | IOException e) {
            throw new RuntimeException(e);
        }

        javaMailSender.send(mimeMessage);
        return true;
    }
    private String readFileContent(String fileName) throws IOException {
        Resource resource = resourceLoader.getResource("classpath:templates/" + fileName);
        return new String(Files.readAllBytes(Paths.get(resource.getURI())));
    }




}
