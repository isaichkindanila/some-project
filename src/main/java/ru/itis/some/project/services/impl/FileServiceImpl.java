package ru.itis.some.project.services.impl;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import ru.itis.some.project.dto.FileDto;
import ru.itis.some.project.dto.FileInfoDto;
import ru.itis.some.project.models.FileInfo;
import ru.itis.some.project.repositories.FileInfoRepository;
import ru.itis.some.project.repositories.FileInputStreamRepository;
import ru.itis.some.project.services.AuthService;
import ru.itis.some.project.services.FileService;
import ru.itis.some.project.services.TokenGeneratorService;

import java.io.InputStream;
import java.util.List;
import java.util.function.Supplier;

@Service
@RequiredArgsConstructor
public class FileServiceImpl implements FileService {

    private final TokenGeneratorService tokenGeneratorService;
    private final AuthService authService;

    private final FileInputStreamRepository urlRepository;
    private final FileInfoRepository infoRepository;

    @Value("${tokens.files.length}")
    private int tokenLength;

    private FileDto dtoFrom(Supplier<InputStream> inputStreamSupplier, FileInfo info) {
        return FileDto.builder()
                .inputStreamSupplier(inputStreamSupplier)
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

        var inputStreamSupplier = urlRepository.create(token, file);

        infoRepository.create(info);

        return dtoFrom(inputStreamSupplier, info);
    }

    @Override
    public FileDto load(String fileName) {
        var info = infoRepository.findByToken(fileName).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND)
        );

        if (info.getUserId() != authService.getCurrentUser().getId()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }

        var inputStreamSupplier = urlRepository.find(fileName).orElseThrow(
                () -> new IllegalStateException("metadata of a file exists in database but it's not present in storage: '" + fileName + "'")
        );

        return dtoFrom(inputStreamSupplier, info);
    }

    @Override
    public List<FileInfoDto> findByCurrentUser() {
        var user = authService.getCurrentUser();
        var infoList = infoRepository.findByUserId(user.getId());

        return FileInfoDto.from(infoList);
    }
}
