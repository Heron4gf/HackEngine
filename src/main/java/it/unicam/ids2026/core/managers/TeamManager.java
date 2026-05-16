package it.unicam.ids2026.core.managers;

import it.unicam.ids2026.core.events.EventPublisher;
import it.unicam.ids2026.core.hackathon.Hackathon;
import it.unicam.ids2026.core.roles.team.Team;
import it.unicam.ids2026.core.roles.team.Utente;
import it.unicam.ids2026.persistence.TeamRepository;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.NoSuchElementException;
import java.util.Set;

/**
 * Gestisce le operazioni relative ai team.
 */
@Service
@RequiredArgsConstructor
public class TeamManager {

    private final TeamRepository teamRepository;
    private final EventPublisher eventPublisher;

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
     * Restituisce il team con il nome specificato all'interno dell'hackathon.
     *
     * <p>La ricerca viene effettuata tramite uno stream sulla lista dei team
     * registrati nell'hackathon. Se nessun team corrisponde al nome fornito,
     * il metodo restituisce {@code null} senza sollevare eccezioni.</p>
     *
     * @param teamName  il nome del team da cercare; non deve essere {@code null}
     * @param hackathon l'hackathon in cui effettuare la ricerca; non deve essere {@code null}
     * @return il team corrispondente al nome indicato, oppure {@code null} se non trovato
     */
    public Team getTeam(@NonNull String teamName, @NonNull Hackathon hackathon) {
        return hackathon.getTeams().stream()
                .filter(team -> team.getNome().equals(teamName))
                .findFirst()
                .orElse(null);
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
        eventPublisher.publishEnter(team, utente);
        teamRepository.save(team);
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
            teamRepository.delete(team);;
        } else {
            teamRepository.save(team);
        }
    }
}
