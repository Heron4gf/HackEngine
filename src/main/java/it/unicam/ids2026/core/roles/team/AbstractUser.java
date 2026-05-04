package it.unicam.ids2026.core.roles.team;

import it.unicam.ids2026.core.roles.User;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.NonNull;

import java.util.UUID;

@RequiredArgsConstructor
@Getter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public abstract class AbstractUser implements User {

    @EqualsAndHashCode.Include
    private final UUID id;

    @NonNull
    private final String nome;

    public AbstractUser(String nome) {
        this(UUID.randomUUID(), nome);
    }
}