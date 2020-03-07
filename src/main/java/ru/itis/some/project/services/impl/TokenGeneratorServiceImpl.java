package ru.itis.some.project.services.impl;

import org.springframework.stereotype.Component;
import ru.itis.some.project.services.TokenGeneratorService;

import java.security.SecureRandom;
import java.util.Base64;

@Component
public class TokenGeneratorServiceImpl implements TokenGeneratorService {

    private final SecureRandom random = new SecureRandom();
    private final Base64.Encoder encoder = Base64.getUrlEncoder();

    @Override
    public String generateToken(int length) {
        if (length <= 0) {
            throw new IllegalArgumentException("[length] must be positive (got " + length + ")");
        }

        var bytes = (length % 4 == 0)
                ? new byte[3 * length / 4]
                : new byte[3 * (1 + length / 4)];

        random.nextBytes(bytes);
        var token = encoder.encodeToString(bytes);

        return token.substring(0, length);
    }
}
