package ru.itis.some.project.services.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailSendException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import ru.itis.some.project.dto.SignUpDto;
import ru.itis.some.project.models.SignUpToken;
import ru.itis.some.project.models.User;
import ru.itis.some.project.repositories.SignUpTokenRepository;
import ru.itis.some.project.repositories.UserRepository;
import ru.itis.some.project.services.EmailService;
import ru.itis.some.project.services.SignUpService;
import ru.itis.some.project.services.TemplateService;
import ru.itis.some.project.util.exceptions.ServiceException;

import java.util.Map;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class SignUpServiceImpl implements SignUpService {
    private final UserRepository userRepository;
    private final SignUpTokenRepository tokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final TemplateService templateService;
    private final EmailService emailService;

    @Value("${server.url}")
    private String serverURL;

    @Override
    public void signUp(SignUpDto dto) {
        if (userRepository.findByEmail(dto.getEmail()).isPresent()) {
            throw new ServiceException("email is already taken");
        }

        var token = UUID.randomUUID().toString();
        var modelMap = Map.of(
                "server", serverURL,
                "token", token
        );

        var message = templateService.process("confirm_sign_up", modelMap);

        try {
            emailService.sendEmail(dto.getEmail(), message);
        } catch (MailSendException e) {
            throw new ServiceException("cannot confirm email");
        }

        var user = User.builder()
                .email(dto.getEmail())
                .passHash(passwordEncoder.encode(dto.getPassword()))
                .isActivated(false)
                .build();

        userRepository.create(user);

        var signUpToken = SignUpToken.builder()
                .token(token)
                .user(user)
                .isUsed(false)
                .build();

        tokenRepository.create(signUpToken);
    }
}
