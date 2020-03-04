package ru.itis.some.project.services;

public interface ConfirmSignUpService {
    boolean confirm(String token);
}
