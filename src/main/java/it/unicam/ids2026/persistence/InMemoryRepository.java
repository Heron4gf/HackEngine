package it.unicam.ids2026.persistence;

import lombok.NonNull;

import java.util.LinkedHashSet;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.function.Function;

abstract class InMemoryRepository<T, ID> implements DomainRepository<T, ID> {

    private final ConcurrentMap<ID, T> entities = new ConcurrentHashMap<>();
    private final Function<T, ID> idExtractor;

    protected InMemoryRepository(Function<T, ID> idExtractor) {
        this.idExtractor = idExtractor;
    }

    @Override
    public T save(@NonNull T entity) {
        entities.put(idExtractor.apply(entity), entity);
        return entity;
    }

    @Override
    public Optional<T> findById(@NonNull ID id) {
        return Optional.ofNullable(entities.get(id));
    }

    @Override
    public Set<T> findAll() {
        return new LinkedHashSet<>(entities.values());
    }

    @Override
    public void delete(@NonNull T entity) {
        entities.remove(idExtractor.apply(entity));
    }

    @Override
    public boolean existsById(@NonNull ID id) {
        return entities.containsKey(id);
    }
}
