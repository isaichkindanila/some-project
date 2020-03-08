package ru.itis.some.project.services;

import org.springframework.web.multipart.MultipartFile;
import ru.itis.some.project.dto.FileDto;
import ru.itis.some.project.dto.FileInfoDto;

import java.util.List;

public interface FileService {

    FileDto save(MultipartFile file);

    FileDto load(String fileName);

    List<FileInfoDto> findByCurrentUser();
}
