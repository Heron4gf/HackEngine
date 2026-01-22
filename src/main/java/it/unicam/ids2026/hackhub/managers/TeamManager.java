package it.unicam.ids2026.hackhub.managers;

import it.unicam.ids2026.hackhub.roles.team.Team;
import it.unicam.ids2026.hackhub.roles.team.Utente;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@RequiredArgsConstructor
public class TeamManager {

    @Getter
    private final Set<Team> teams;

    public Team getTeam(@NonNull String teamName) {
        Team toReturn = null;
        for (Team team : teams) {
            if (team.getNome().equals(teamName)) {
                toReturn = team;
            }
        }
        return toReturn;
    }

    public void addTeam(@NonNull Team team) {
        teams.add(team);
    }

    public void removeTeam(@NonNull Team team) {
        teams.remove(team);
    }

    public void creaTeam(@NonNull Utente utente, @NonNull String nome, int maxMembri) {
        if(utente.haTeam()) {
            throw new IllegalArgumentException("L'utente ha già un team");
        }
        if(getTeam(nome) != null) {
            throw new IllegalArgumentException("Esiste già un team con lo stesso nome");
        }
        if(maxMembri < 0 || maxMembri > 20) {
            throw new IllegalArgumentException("Non puoi creare un team con più di 20 o meno di zero membri");
        }
        Team team = new Team(nome, maxMembri, new HashSet<>(Set.of(utente)));
        utente.setTeam(team);
        addTeam(team);
    }

    public void esciDalTeam(@NonNull Utente utente) {
        if(!utente.haTeam()) {
            throw new IllegalArgumentException("L'Utente non ha team!");
        }
        Team team = utente.getTeam();
        utente.setTeam(null);
        team.getMembri().remove(utente);
        if(team.getMembri().isEmpty()) {
            removeTeam(team);
        }
    }

}