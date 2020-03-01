package ru.itis.some.project.services.impl;

import lombok.AllArgsConstructor;
import org.springframework.core.env.Environment;
import org.springframework.mail.MailSendException;
import org.springframework.stereotype.Component;
import ru.itis.some.project.dto.SignUpDto;
import ru.itis.some.project.models.SignUpToken;
import ru.itis.some.project.models.User;
import ru.itis.some.project.repositories.SignUpTokenRepository;
import ru.itis.some.project.repositories.UserRepository;
import ru.itis.some.project.services.EmailService;
import ru.itis.some.project.services.SignUpService;
import ru.itis.some.project.util.auth.PasswordEncoder;
import ru.itis.some.project.util.exceptions.ServiceException;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Component
@AllArgsConstructor
public class SignUpServiceImpl implements SignUpService {
    private final UserRepository userRepository;
    private final SignUpTokenRepository tokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;
    private final Environment environment;

    @Override
    public void signUp(SignUpDto dto) {
        if (userRepository.findByEmail(dto.getEmail()).isPresent()) {
            throw new ServiceException("email is already taken");
        }

        String token = UUID.randomUUID().toString();
        Map<String, Object> modelMap = new HashMap<>();

        modelMap.put("server", environment.getRequiredProperty("server.url"));
        modelMap.put("token", token);

        try {
            emailService.sendEmail(dto.getEmail(), "confirm_sign_up", modelMap);
        } catch (MailSendException e) {
            throw new ServiceException("cannot confirm email");
        }

        User user = User.builder()
                .email(dto.getEmail())
                .passHash(passwordEncoder.hash(dto.getPassword()))
                .isActivated(false)
                .build();

        userRepository.create(user);

        SignUpToken signUpToken = SignUpToken.builder()
                .token(token)
                .user(user)
                .build();

        tokenRepository.create(signUpToken);
    }
}
