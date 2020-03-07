package ru.itis.some.project.services.impl;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import ru.itis.some.project.dto.FileDto;
import ru.itis.some.project.models.FileInfo;
import ru.itis.some.project.repositories.FileInfoRepository;
import ru.itis.some.project.services.FileService;
import ru.itis.some.project.services.TokenGeneratorService;

import java.io.File;

@Service
@RequiredArgsConstructor
public class FileServiceImpl implements FileService {

    private final TokenGeneratorService tokenGeneratorService;
    private final FileInfoRepository infoRepository;

    @Value("${storage.path}")
    private String storagePath;

    @Value("${tokens.files.length}")
    private int tokenLength;

    private File getFileInStorage(String fileName) {
        return new File(storagePath, fileName);
    }

    @SneakyThrows
    private FileDto dtoFrom(File file, FileInfo info) {
        return FileDto.builder()
                .url(file.toURI().toURL())
                .length(info.getLength())
                .token(info.getToken())
                .mimeType(info.getMimeType())
                .originalName(info.getOriginalName())
                .build();
    }

    @Override
    @SneakyThrows
    public FileDto save(MultipartFile file) {
        var token = tokenGeneratorService.generateToken(tokenLength);
        var info = FileInfo.builder()
                .token(token)
                .length(file.getSize())
                .mimeType(file.getContentType())
                .originalName(file.getOriginalFilename())
                .build();

        infoRepository.create(info);

        var storageFile = getFileInStorage(token);
        file.transferTo(storageFile);

        return dtoFrom(storageFile, info);
    }

    @Override
    public FileDto load(String fileName) {
        var optional = infoRepository.findByToken(fileName);

        if (optional.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }

        var file = new File(storagePath, fileName);
        var info = optional.get();

        if (!file.exists()) {
            throw new IllegalStateException("file exists in database but not on disk: " + fileName);
        }

        return dtoFrom(file, info);
    }
}
