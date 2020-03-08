package ru.itis.some.project.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class FileInfo {
    private long id;
    private long length;
    private long userId;
    private String token;
    private String mimeType;
    private String originalName;
}
