package ru.itis.some.project.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class SignUpToken {
    private String token;
    private long userId;
    private boolean isUsed;
}
