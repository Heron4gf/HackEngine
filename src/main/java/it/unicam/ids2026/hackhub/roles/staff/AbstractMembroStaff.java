package it.unicam.ids2026.hackhub.roles.staff;

import it.unicam.ids2026.hackhub.hackathon.Hackathon;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@RequiredArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public abstract class AbstractMembroStaff implements MembroStaff {

    @EqualsAndHashCode.Include
    private final UUID id;

    private final String nome;
    private final String cognome;

    @Setter
    private Hackathon associatedHackathon;
}