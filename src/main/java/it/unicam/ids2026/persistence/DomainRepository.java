package it.unicam.ids2026.persistence;

import lombok.NonNull;

import java.util.Optional;
import java.util.Set;

public interface DomainRepository<T, ID> {

    T save(@NonNull T entity);

    Optional<T> findById(@NonNull ID id);

    Set<T> findAll();

    void delete(@NonNull T entity);

    boolean existsById(@NonNull ID id);
}
