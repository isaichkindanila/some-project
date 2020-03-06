package ru.itis.some.project.services.impl;

import lombok.AllArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import ru.itis.some.project.services.EmailService;

import javax.mail.MessagingException;

@Component
@AllArgsConstructor
public class EmailServiceImpl implements EmailService {
    private final JavaMailSender mailSender;

    @Override
    public void sendEmail(String email, String msg) {
        try {
            var message = mailSender.createMimeMessage();
            var helper = new MimeMessageHelper(message);

            helper.setTo(email);
            helper.setSubject("Email verification");
            helper.setText(msg, true);

            mailSender.send(message);
        } catch (MessagingException e) {
            throw new IllegalStateException(e);
        }
    }
}
