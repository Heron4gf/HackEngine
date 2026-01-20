package it.unicam.ids2026.hackhub.roles;
import it.unicam.ids2026.hackhub.HackHub;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

import java.util.HashSet;
import java.util.Set;


@Getter
public class Team {
    private int maxMembri;
    private String nome;
    private int numeroMembri = 0;
    private Set<Utente> membri;

    public Team(@NotNull String nome, int maxMembri) {
        this.nome = nome;
        this.maxMembri = maxMembri;
    }


    void aggiungiUtenti(@NotNull Utente utente) {
        if (numeroMembri <= maxMembri) {
            membri.add(utente);
            numeroMembri++;
        }
        else {
            throw new IllegalArgumentException("Numero massimo superato");
        }
    }

    void esciDalTeam(Utente utente) {
        membri.remove(utente);
        numeroMembri--;
        if (numeroMembri == 0) {
            HackHub.getInstance().getAllTeams().remove(this);
        }
    }



}
