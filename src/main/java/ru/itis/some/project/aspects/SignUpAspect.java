package ru.itis.some.project.aspects;

import lombok.RequiredArgsConstructor;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import ru.itis.some.project.dto.SignUpDto;
import ru.itis.some.project.services.EmailService;
import ru.itis.some.project.services.TemplateService;

import java.util.Map;

@Aspect
@Component
@RequiredArgsConstructor
public class SignUpAspect {

    private final TemplateService templateService;
    private final EmailService emailService;

    @Value("${server.url}")
    private String serverURL;

    @AfterReturning(
            pointcut = "execution(* ru.itis.some.project.services.SignUpService.signUp(..)) && args(dto,..)",
            returning = "token",
            argNames = "dto,token"
    )
    public void sendConformationEmail(SignUpDto dto, String token) {
        var modelMap = Map.of(
                "server", serverURL,
                "token", token
        );

        var message = templateService.process("confirm_sign_up", modelMap);

        emailService.sendEmail(dto.getEmail(), "Email conformation", message);
    }
}
