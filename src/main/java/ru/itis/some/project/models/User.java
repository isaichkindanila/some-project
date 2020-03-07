package ru.itis.some.project.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class User {
    private Long id;
    private String email;
    private String passHash;
    private boolean isActivated;
}
