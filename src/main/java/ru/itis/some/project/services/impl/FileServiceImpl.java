package ru.itis.some.project.services.impl;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import ru.itis.some.project.dto.FileDto;
import ru.itis.some.project.dto.FileInfoDto;
import ru.itis.some.project.models.FileInfo;
import ru.itis.some.project.repositories.FileInfoRepository;
import ru.itis.some.project.repositories.FileResourceRepository;
import ru.itis.some.project.services.AuthService;
import ru.itis.some.project.services.FileService;
import ru.itis.some.project.services.TokenGeneratorService;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FileServiceImpl implements FileService {

    private final TokenGeneratorService tokenGeneratorService;
    private final AuthService authService;

    private final FileResourceRepository resourceRepository;
    private final FileInfoRepository infoRepository;

    @Value("${tokens.files.length}")
    private int tokenLength;

    private FileDto dtoFrom(Resource resource, FileInfo info) {
        return FileDto.builder()
                .resource(resource)
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
                .userId(authService.getCurrentUser().getId())
                .mimeType(file.getContentType())
                .originalName(file.getOriginalFilename())
                .build();

        infoRepository.create(info);

        var resource = resourceRepository.create(token, file);

        return dtoFrom(resource, info);
    }

    @Override
    public FileDto load(String fileName) {
        var info = infoRepository.findByToken(fileName).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND)
        );

        if (info.getUserId() != authService.getCurrentUser().getId()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }

        var resource = resourceRepository.find(fileName).orElseThrow(
                () -> new IllegalStateException("metadata of a file exists in database but it's not present in storage: '" + fileName + "'")
        );

        return dtoFrom(resource, info);
    }

    @Override
    public List<FileInfoDto> findByCurrentUser() {
        var user = authService.getCurrentUser();
        var infoList = infoRepository.findByUserId(user.getId());

        return FileInfoDto.from(infoList);
    }
}
