package ru.itis.some.project.repositories.impl;

import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
import org.springframework.web.multipart.MultipartFile;
import ru.itis.some.project.repositories.FileInputStreamRepository;

import java.io.File;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.util.Optional;
import java.util.function.Supplier;

@Repository
public class FileInputStreamRepositoryLocalStorageImpl implements FileInputStreamRepository {

    private final File storage;

    public FileInputStreamRepositoryLocalStorageImpl(@Value("${storage.path}") String storagePath) {
        storage = new File(storagePath);
    }

    @Override
    @SneakyThrows
    public Optional<Supplier<InputStream>> find(String token) {
        var file = new File(storage, token);

        if (file.exists() && file.isFile()) {
            var url = file.toURI().toURL();
            return Optional.of(new URLStreamSupplier(url));
        } else {
            return Optional.empty();
        }
    }

    @Override
    @SneakyThrows
    public Supplier<InputStream> create(String token, MultipartFile file) {
        var fileInStorage = new File(storage, token);
        file.transferTo(fileInStorage);

        var url = fileInStorage.toURI().toURL();
        return new URLStreamSupplier(url);
    }

    @Override
    @SneakyThrows
    public void delete(String token) {
        var file = new File(storage, token);
        Files.deleteIfExists(file.toPath());
    }

    @AllArgsConstructor
    private static class URLStreamSupplier implements Supplier<InputStream> {

        private final URL url;

        @Override
        @SneakyThrows
        public InputStream get() {
            return url.openStream();
        }
    }
}
