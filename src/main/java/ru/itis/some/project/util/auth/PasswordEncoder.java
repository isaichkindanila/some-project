package ru.itis.some.project.util.auth;

public interface PasswordEncoder {

    String hash(String password);

    boolean check(String password, String hash);
}
