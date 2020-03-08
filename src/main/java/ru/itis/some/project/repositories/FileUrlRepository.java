package ru.itis.some.project.repositories;

import org.springframework.web.multipart.MultipartFile;

import java.net.URL;
import java.util.Optional;

public interface FileUrlRepository {

    Optional<URL> find(String token);

    URL create(String token, MultipartFile file);

    void delete(String token);
}
