package it.unicam.ids2026.hackhub.roles.staff;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.UUID;

@Getter
@RequiredArgsConstructor
public abstract class AbstractMembroStaff implements MembroStaff {

    private final UUID id;
    private final String nome;
    private final String cognome;

}
