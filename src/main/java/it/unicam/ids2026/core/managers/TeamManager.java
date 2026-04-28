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
import java.util.UUID;

/**
 * Gestisce le operazioni relative ai team.
 */
@Service
public class TeamManager {

    private final TeamRepository teamRepository;
    private final EventPublisher eventPublisher;

    @Autowired
    public TeamManager(TeamRepository teamRepository, EventPublisher eventPublisher) {
        this.teamRepository = teamRepository;
        this.eventPublisher = eventPublisher;
    }

    /**
     * Recupera un team tramite il suo nome.
     *
     * @param teamName nome del team
     * @return il team cercato
     * @throws NoSuchElementException se il team non esiste
     */
    public Team getTeam(@NonNull String teamName) {
        return teamRepository.findById(teamName)
                .orElseThrow(() -> new NoSuchElementException("Nessun team trovato con nome: " + teamName));
    }

    /**
     * Restituisce tutti i team presenti nel sistema.
     *
     * @return insieme di tutti i team
     */
    public Set<Team> getTeams() {
        return teamRepository.findAll();
    }

    /**
     * Salva un team nel repository.
     *
     * @param team team da salvare
     */
    public void addTeam(@NonNull Team team) {
        teamRepository.save(team);
    }

    /**
     * Rimuove un team dal repository.
     *
     * @param team team da rimuovere
     */
    public void removeTeam(@NonNull Team team) {
        teamRepository.delete(team);
    }

    /**
     * Crea un nuovo team e assegna l'utente come primo membro.
     * L'utente non deve già appartenere a un team.
     *
     * @param utente utente che crea il team (diventa il primo membro)
     * @param nome nome del team
     * @param maxMembri numero massimo di membri
     * @throws IllegalArgumentException se l'utente ha già un team, il nome è duplicato, o maxMembri non è valido
     */
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

    /**
     * Rimuove un utente dal suo team.
     * Se il team rimane vuoto, viene eliminato e viene pubblicato un evento di eliminazione.
     *
     * @param utente utente che esce dal team
     * @throws IllegalArgumentException se l'utente non ha un team
     */
    public void esciDalTeam(@NonNull Utente utente) {
        if (!utente.haTeam()) {
            throw new IllegalArgumentException("L'Utente non ha team!");
        }
        Team team = utente.getTeam();
        utente.setTeam(null);
        team.getMembri().remove(utente);
        if (team.getMembri().isEmpty()) {
            // Pubblica evento e rimuove il team se non ci sono più membri
            eventPublisher.publishDeletion(team);
            removeTeam(team);
        } else {
            teamRepository.save(team);
        }
    }
}
