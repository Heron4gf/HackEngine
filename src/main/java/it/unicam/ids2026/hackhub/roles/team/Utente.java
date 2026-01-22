package it.unicam.ids2026.hackhub.roles.team;

import it.unicam.ids2026.hackhub.HackHub;

import java.util.Collection;
import java.util.LinkedList;
import java.util.UUID;

public class Utente extends AbstractUser {
    private Team teamCorrente;
    private Collection<Invito> casella_inviti;

    public Utente(String nome) {
        this(UUID.randomUUID(), nome);
    }

    public Utente(UUID id, String nome) {
        this(id, nome, null, new LinkedList<>());
    }

    public Utente(UUID id, String nome, Team teamCorrente, Collection<Invito> casella_inviti) {
        super(id, nome);
        this.teamCorrente = teamCorrente;
        this.casella_inviti = casella_inviti;
    }


    public void setTeam(Team team) {
        this.teamCorrente = team;
    }

    public boolean haTeam() {
        return this.teamCorrente != null;
    }
}
