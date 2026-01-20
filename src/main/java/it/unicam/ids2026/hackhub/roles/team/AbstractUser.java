package it.unicam.ids2026.hackhub.roles.team;

import it.unicam.ids2026.hackhub.roles.User;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.UUID;

import it.unicam.ids2026.hackhub.roles.User;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.UUID;

@RequiredArgsConstructor
@Getter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public abstract class AbstractUser implements User {

    @EqualsAndHashCode.Include
    private final UUID id;

    private final String nome;
}