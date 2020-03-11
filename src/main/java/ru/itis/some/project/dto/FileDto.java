package ru.itis.some.project.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.springframework.core.io.Resource;

@Data
@Builder
@AllArgsConstructor
public class FileDto {
    private final Long length;
    private final String token;
    private final String mimeType;
    private final String originalName;
    private final Resource resource;
}
