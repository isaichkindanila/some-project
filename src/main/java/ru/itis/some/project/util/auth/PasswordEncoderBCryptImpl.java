package ru.itis.some.project.util.auth;

import org.mindrot.jbcrypt.BCrypt;
import org.springframework.stereotype.Component;

@Component
public class PasswordEncoderBCryptImpl implements PasswordEncoder {

    @Override
    public String hash(String password) {
        return BCrypt.hashpw(password, BCrypt.gensalt());
    }

    @Override
    public boolean check(String password, String hash) {
        return BCrypt.checkpw(password, hash);
    }
}
