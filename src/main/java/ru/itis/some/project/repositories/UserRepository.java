package ru.itis.some.project.repositories;

import ru.itis.some.project.models.User;

import java.util.Optional;

public interface UserRepository extends Repository<User, Long> {

    Optional<User> findByEmail(String email);
}
