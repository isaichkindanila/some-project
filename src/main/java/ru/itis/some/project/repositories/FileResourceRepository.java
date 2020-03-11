package ru.itis.some.project.repositories;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

public interface FileResourceRepository {

    Optional<Resource> find(String token);

    Resource create(String token, MultipartFile file);

    void delete(String token);
}
