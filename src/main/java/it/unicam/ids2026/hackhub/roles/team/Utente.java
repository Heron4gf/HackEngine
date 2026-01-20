package it.unicam.ids2026.hackhub.roles.team;

import it.unicam.ids2026.hackhub.roles.invito.Invito;

import java.util.Collection;
import java.util.UUID;

public class Utente extends AbstractUser {
    private Team teamCorrente;
    private Collection<Invito> casella_inviti;

    public Utente(UUID id, String nome) {
        super(id, nome);
    }

    public Team creaTeam(String nome, int maxMembri) {
        Team team = new Team(nome, maxMembri);
        team.aggiungiUtenti(this);
        return team;
    }

    public void riceviInvito(Invito invito) {
        if (!haTeam()) casella_inviti.add(invito);
        throw new IllegalArgumentException("l'utente "+this.getNome()+" ha già un team");
    }

    public void setTeam(Team team) {
        this.teamCorrente = team;
    }

    public boolean haTeam() {
        return this.teamCorrente != null;
    }
}
