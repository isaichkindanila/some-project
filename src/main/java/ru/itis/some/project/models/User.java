package ru.itis.some.project.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class User {
    private Long id;
    private String email;
    private String passHash;
    private boolean isActivated;
}
