package ru.itis.some.project.services;

import ru.itis.some.project.dto.SignUpDto;

public interface SignUpService {

    /**
     * @return email conformation token in {@code String} form
     */
    String signUp(SignUpDto dto);
}
