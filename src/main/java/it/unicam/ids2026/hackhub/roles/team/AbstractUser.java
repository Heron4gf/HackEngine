package it.unicam.ids2026.hackhub.roles.team;

import it.unicam.ids2026.hackhub.roles.User;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.UUID;

@RequiredArgsConstructor
@Getter
public abstract class AbstractUser implements User {
    private final UUID id;
    private final String nome;
}
