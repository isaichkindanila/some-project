package ru.itis.some.project.repositories.impl;

import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Repository;
import org.springframework.web.multipart.MultipartFile;
import ru.itis.some.project.repositories.FileResourceRepository;

import java.io.File;
import java.nio.file.Files;
import java.util.Optional;

@Repository
public class FileResourceRepositoryLocalStorageImpl implements FileResourceRepository {

    private final File storage;

    public FileResourceRepositoryLocalStorageImpl(@Value("${storage.path}") String storagePath) {
        storage = new File(storagePath);
    }

    @Override
    @SneakyThrows
    public Optional<Resource> find(String token) {
        var file = new File(storage, token);

        if (file.exists() && file.isFile()) {
            return Optional.of(new FileSystemResource(file));
        } else {
            return Optional.empty();
        }
    }

    @Override
    @SneakyThrows
    public Resource create(String token, MultipartFile file) {
        var fileInStorage = new File(storage, token);
        file.transferTo(fileInStorage);

        return new FileSystemResource(fileInStorage);
    }

    @Override
    @SneakyThrows
    public void delete(String token) {
        var file = new File(storage, token);
        Files.deleteIfExists(file.toPath());
    }
}
