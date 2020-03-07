package ru.itis.some.project.repositories;

import ru.itis.some.project.models.FileInfo;

import java.util.Optional;

public interface FileInfoRepository extends Repository<FileInfo, Long> {
    Optional<FileInfo> findByToken(String token);
}
