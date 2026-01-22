package it.unicam.ids2026.hackhub.roles.team;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;

import java.util.HashSet;
import java.util.Set;


@Getter
@Setter
@AllArgsConstructor
public class Team {
    private String nome;
    private int maxMembri;
    private final Set<Utente> membri;

    public Team(@NotNull String nome, int maxMembri) {
        this(nome, maxMembri, new HashSet<>());
    }

}