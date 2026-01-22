package it.unicam.ids2026.hackhub.roles.team;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;

import java.util.Set;


@Getter
@Setter
@AllArgsConstructor
public class Team {
    private int maxMembri;
    private String nome;
    private int numeroMembri = 0;
    private Set<Utente> membri;

    public Team(@NotNull String nome, int maxMembri) {
        this.nome = nome;
        this.maxMembri = maxMembri;
    }

}