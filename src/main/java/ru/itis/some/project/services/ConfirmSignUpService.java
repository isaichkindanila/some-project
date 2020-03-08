package ru.itis.some.project.services;

public interface ConfirmSignUpService {

    /**
     * @return {@code true} if conformation was successful,
     *         {@code false} if token was already used
     */
    boolean confirm(String token);
}
