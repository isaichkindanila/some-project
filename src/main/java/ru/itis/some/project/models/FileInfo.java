package ru.itis.some.project.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class FileInfo {
    private Long id;
    private Long length;
    private String token;
    private String mimeType;
    private String originalName;
}
