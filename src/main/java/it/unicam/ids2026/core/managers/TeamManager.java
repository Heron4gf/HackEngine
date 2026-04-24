package it.unicam.ids2026.core.managers;

import it.unicam.ids2026.core.events.EventPublisher;
import it.unicam.ids2026.core.roles.team.Team;
import it.unicam.ids2026.core.roles.team.Utente;
import it.unicam.ids2026.persistence.TeamRepository;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.NoSuchElementException;
import java.util.Set;

@Service
public class TeamManager {

    private final TeamRepository teamRepository;
    private final EventPublisher eventPublisher;

    @Autowired
    public TeamManager(TeamRepository teamRepository, EventPublisher eventPublisher) {
        this.teamRepository = teamRepository;
        this.eventPublisher = eventPublisher;
    }

    public Team getTeam(@NonNull String teamName) {
        return teamRepository.findById(teamName)
                .orElseThrow(() -> new NoSuchElementException("Nessun team trovato con nome: " + teamName));
    }

    public Set<Team> getTeams() {
        return teamRepository.findAll();
    }

    public void addTeam(@NonNull Team team) {
        teamRepository.save(team);
    }

    public void removeTeam(@NonNull Team team) {
        teamRepository.delete(team);
    }

    public void creaTeam(@NonNull Utente utente, @NonNull String nome, int maxMembri) {
        if (utente.haTeam()) {
            throw new IllegalArgumentException("L'utente ha gia un team");
        }
        if (teamRepository.existsById(nome)) {
            throw new IllegalArgumentException("Esiste gia un team con lo stesso nome");
        }
        if (maxMembri < 1 || maxMembri > 20) {
            throw new IllegalArgumentException("Numero massimo di membri non valido (deve essere tra 1 e 20)");
        }
        Team team = new Team(nome, maxMembri, new HashSet<>(Set.of(utente)));
        utente.setTeam(team);
        addTeam(team);
    }

    public void esciDalTeam(@NonNull Utente utente) {
        if (!utente.haTeam()) {
            throw new IllegalArgumentException("L'Utente non ha team!");
        }
        Team team = utente.getTeam();
        utente.setTeam(null);
        team.getMembri().remove(utente);
        if (team.getMembri().isEmpty()) {
            eventPublisher.publishDeletion(team);
            removeTeam(team);
        } else {
            teamRepository.save(team);
        }
    }
}
