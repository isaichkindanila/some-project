package ru.itis.some.project.repositories;

import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.Optional;
import java.util.function.Supplier;

public interface FileInputStreamRepository {

    Optional<Supplier<InputStream>> find(String token);

    Supplier<InputStream> create(String token, MultipartFile file);

    void delete(String token);
}
