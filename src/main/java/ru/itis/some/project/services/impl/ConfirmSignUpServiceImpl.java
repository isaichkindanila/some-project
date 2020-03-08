package ru.itis.some.project.services.impl;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.itis.some.project.repositories.SignUpTokenRepository;
import ru.itis.some.project.repositories.UserRepository;
import ru.itis.some.project.services.ConfirmSignUpService;
import ru.itis.some.project.util.exceptions.ServiceException;

@Service
@AllArgsConstructor
public class ConfirmSignUpServiceImpl implements ConfirmSignUpService {
    private final UserRepository userRepository;
    private final SignUpTokenRepository tokenRepository;

    @Override
    public boolean confirm(String token) {
        var signUpToken = tokenRepository.find(token).orElseThrow(
                () -> new ServiceException("token not found")
        );

        if (signUpToken.isUsed()) {
            // user has already confirmed email
            return false;
        }

        var user = userRepository.find(signUpToken.getUserId()).orElseThrow(
                () -> new IllegalStateException("user not found (id = " + signUpToken.getUserId() + ")")
        );

        user.setActivated(true);
        signUpToken.setUsed(true);

        userRepository.update(user);
        tokenRepository.update(signUpToken);

        // user has confirmed email
        return true;
    }
}
