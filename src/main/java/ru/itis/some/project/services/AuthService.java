package ru.itis.some.project.services;

import org.springframework.lang.NonNull;
import ru.itis.some.project.models.User;

import java.util.Optional;

public interface AuthService {

    Optional<User> getCurrentUserOptional();

    /**
     * @return currently authenticated user
     * @throws IllegalStateException if user is not authenticated
     */
    @NonNull
    User getCurrentUser();
}
