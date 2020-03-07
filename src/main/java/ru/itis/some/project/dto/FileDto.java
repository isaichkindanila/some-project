package ru.itis.some.project.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.net.URL;

@Data
@Builder
@AllArgsConstructor
public class FileDto {
    private final URL url;
    private final Long length;
    private final String mimeType;
    private final String originalName;
}
