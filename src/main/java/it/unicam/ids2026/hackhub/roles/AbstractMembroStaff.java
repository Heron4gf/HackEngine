package it.unicam.ids2026.hackhub.roles;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.UUID;

@Getter
@AllArgsConstructor
public abstract class AbstractMembroStaff implements MembroStaff {

    private String nome;
    private String cognome;
    private UUID id;

}
