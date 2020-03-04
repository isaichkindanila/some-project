package ru.itis.some.project.services.impl;

import freemarker.template.Configuration;
import freemarker.template.TemplateException;
import lombok.AllArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import ru.itis.some.project.services.EmailService;

import javax.mail.MessagingException;
import java.io.IOException;
import java.io.StringWriter;
import java.util.Map;

@Component
@AllArgsConstructor
public class EmailServiceImpl implements EmailService {
    private final Configuration config;
    private final JavaMailSender mailSender;

    @Override
    public void sendEmail(String email, String templateName, Map<String, ?> modelMap) {
        try(StringWriter writer = new StringWriter()) {
            var template = config.getTemplate(templateName + ".ftl");
            template.process(modelMap, writer);

            var message = mailSender.createMimeMessage();
            var helper = new MimeMessageHelper(message);

            helper.setTo(email);
            helper.setSubject("Email verification");
            helper.setText(writer.toString(), true);

            mailSender.send(message);
        } catch (IOException | TemplateException | MessagingException e) {
            throw new IllegalStateException(e);
        }
    }
}
