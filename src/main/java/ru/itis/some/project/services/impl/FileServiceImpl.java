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
import ru.itis.some.project.repositories.FileUrlRepository;
import ru.itis.some.project.services.FileService;
import ru.itis.some.project.services.TokenGeneratorService;

import java.net.URL;

@Service
@RequiredArgsConstructor
public class FileServiceImpl implements FileService {

    private final TokenGeneratorService tokenGeneratorService;
    private final FileInfoRepository infoRepository;
    private final FileUrlRepository urlRepository;

    @Value("${tokens.files.length}")
    private int tokenLength;

    private FileDto dtoFrom(URL url, FileInfo info) {
        return FileDto.builder()
                .url(url)
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
        var url = urlRepository.create(token, file);

        var info = FileInfo.builder()
                .token(token)
                .length(file.getSize())
                .mimeType(file.getContentType())
                .originalName(file.getOriginalFilename())
                .build();

        infoRepository.create(info);

        return dtoFrom(url, info);
    }

    @Override
    public FileDto load(String fileName) {
        var optionalInfo = infoRepository.findByToken(fileName);

        if (optionalInfo.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }

        var optionalURL = urlRepository.find(fileName);

        if (optionalURL.isEmpty()) {
            throw new IllegalStateException("metadata of a file exists in database but it's not present in storage: '" + fileName + "'");
        }

        return dtoFrom(optionalURL.get(), optionalInfo.get());
    }
}
