package it.unicam.ids2026.core.roles.team;

import it.unicam.ids2026.core.roles.User;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.NonNull;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.util.UUID;

@RequiredArgsConstructor
@Getter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public abstract class AbstractUser implements User {

    @EqualsAndHashCode.Include
    private final UUID id;

    @NonNull
    @NotBlank
    @Size(min = 3, max = 30, message = "Il nome deve avere tra 3 e 30 caratteri")
    private final String nome;

    public AbstractUser(String nome) {
        this(UUID.randomUUID(), nome);
    }
}