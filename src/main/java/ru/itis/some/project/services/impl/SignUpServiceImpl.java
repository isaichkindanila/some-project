package ru.itis.some.project.services.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import ru.itis.some.project.dto.SignUpDto;
import ru.itis.some.project.models.SignUpToken;
import ru.itis.some.project.models.User;
import ru.itis.some.project.repositories.SignUpTokenRepository;
import ru.itis.some.project.repositories.UserRepository;
import ru.itis.some.project.services.SignUpService;
import ru.itis.some.project.services.TokenGeneratorService;
import ru.itis.some.project.util.exceptions.ServiceException;

@Component
@RequiredArgsConstructor
public class SignUpServiceImpl implements SignUpService {
    private final UserRepository userRepository;
    private final SignUpTokenRepository tokenRepository;
    private final TokenGeneratorService tokenGeneratorService;
    private final PasswordEncoder passwordEncoder;

    @Value("${tokens.signUp.length}")
    private int tokenLength;

    @Override
    public String signUp(SignUpDto dto) {
        if (userRepository.findByEmail(dto.getEmail()).isPresent()) {
            throw new ServiceException("email is already taken");
        }

        var user = User.builder()
                .email(dto.getEmail())
                .passHash(passwordEncoder.encode(dto.getPassword()))
                .isActivated(false)
                .build();

        userRepository.create(user);

        var token = tokenGeneratorService.generateToken(tokenLength);
        var signUpToken = SignUpToken.builder()
                .token(token)
                .userId(user.getId())
                .isUsed(false)
                .build();

        tokenRepository.create(signUpToken);

        return token;
    }
}
