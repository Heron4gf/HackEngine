package it.unicam.ids2026.hackhub.roles;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Set;

@RequiredArgsConstructor
@Getter
public class Team {
    private final int maxMembri;
    private final String nome;

    private Set<Utente> membri;
}
