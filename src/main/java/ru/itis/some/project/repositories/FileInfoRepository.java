package ru.itis.some.project.repositories;

import ru.itis.some.project.models.FileInfo;

import java.util.List;
import java.util.Optional;

public interface FileInfoRepository extends Repository<FileInfo, Long> {

    Optional<FileInfo> findByToken(String token);

    List<FileInfo> findByUserId(Long id);
}
