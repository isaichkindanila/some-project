package ru.itis.some.project.repositories;

import java.util.List;
import java.util.Optional;

public interface Repository<T, ID> {
    Optional<T> find(ID id);
    List<T> findAll();
    void create(T model);
    void update(T model);
    void delete(ID id);
}
