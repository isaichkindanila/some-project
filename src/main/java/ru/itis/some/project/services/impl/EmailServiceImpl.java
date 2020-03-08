package ru.itis.some.project.services.impl;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailSendException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import ru.itis.some.project.services.EmailService;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.util.concurrent.Executor;

@Component
@RequiredArgsConstructor
@Log4j
public class EmailServiceImpl implements EmailService {

    private final JavaMailSender mailSender;
    private final Executor executor;

    @Value("${email.retries}")
    private int retries;

    @Override
    public void sendEmail(String email, String subject, String html) {
        var message = mailSender.createMimeMessage();

        try {
            var helper = new MimeMessageHelper(message);

            helper.setTo(email);
            helper.setSubject(subject);
            helper.setText(html, true);
        } catch (MessagingException e) {
            throw new IllegalStateException(e);
        }

        executor.execute(new SendEmailTask(message));
    }

    @AllArgsConstructor
    private class SendEmailTask implements Runnable {

        private final MimeMessage message;

        @Override
        public void run() {
            for (int i = 0; i < retries; i++) {
                try {
                    mailSender.send(message);
                    return;
                } catch (MailSendException e) {
                    if (i == retries) {
                        log.error("failed to send email", e);
                    }
                }
            }
        }
    }
}
