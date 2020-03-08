package ru.itis.some.project.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import ru.itis.some.project.models.FileInfo;

import java.util.List;
import java.util.stream.Collectors;

@Data
@Builder
@AllArgsConstructor
public class FileInfoDto {
    private final String name;
    private final String token;

    public static FileInfoDto from(FileInfo info) {
        return builder()
                .name(info.getOriginalName())
                .token(info.getToken())
                .build();
    }

    public static List<FileInfoDto> from(List<FileInfo> infoList) {
        return infoList.stream()
                .map(FileInfoDto::from)
                .collect(Collectors.toList());
    }
}
