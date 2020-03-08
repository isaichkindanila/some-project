package ru.itis.some.project.repositories.impl;

import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
import org.springframework.web.multipart.MultipartFile;
import ru.itis.some.project.repositories.FileUrlRepository;

import java.io.File;
import java.net.URL;
import java.nio.file.Files;
import java.util.Optional;

@Repository
public class FileUrlRepositoryLocalStorageImpl implements FileUrlRepository {

    private final File storage;

    public FileUrlRepositoryLocalStorageImpl(@Value("${storage.path}") String storagePath) {
        storage = new File(storagePath);
    }

    @Override
    @SneakyThrows
    public Optional<URL> find(String token) {
        var file = new File(storage, token);

        if (file.exists() && file.isFile()) {
            return Optional.of(file.toURI().toURL());
        } else {
            return Optional.empty();
        }
    }

    @Override
    @SneakyThrows
    public URL create(String token, MultipartFile file) {
        var fileInStorage = new File(storage, token);
        file.transferTo(fileInStorage);

        return fileInStorage.toURI().toURL();
    }

    @Override
    @SneakyThrows
    public void delete(String token) {
        var file = new File(storage, token);
        Files.deleteIfExists(file.toPath());
    }
}
