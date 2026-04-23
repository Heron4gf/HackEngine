package it.unicam.ids2026.core.managers;

import it.unicam.ids2026.core.events.EventPublisher;
import it.unicam.ids2026.core.roles.team.Team;
import it.unicam.ids2026.core.roles.team.Utente;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class TeamManager {

    @Getter
    private final Set<Team> teams;
    private final EventPublisher eventPublisher;

    /**
     * Recupera un team esistente tramite il suo nome univoco.
     *
     * @param teamName Il nome del team da cercare.
     * @return L'oggetto Team se trovato, altrimenti null.
     */
    public Team getTeam(@NonNull String teamName) {
        return teams.stream()
                .filter(team -> team.getNome().equals(teamName))
                .findFirst()
                .orElse(null);
    }

    /**
     * Registra manualmente un team nel sistema.
     *
     * @param team Il team da aggiungere.
     */
    public void addTeam(@NonNull Team team) {
        teams.add(team);
    }

    /**
     * Rimuove un team dal sistema.
     *
     * @param team Il team da rimuovere.
     */
    public void removeTeam(@NonNull Team team) {
        teams.remove(team);
    }

    /**
     * Crea un nuovo team con l'utente specificato come primo membro.
     * Verifica che l'utente non abbia già un team, che il nome sia univoco
     * e che il numero massimo di membri sia valido (0-20).
     *
     * @param utente    L'utente che crea il team.
     * @param nome      Il nome univoco del nuovo team.
     * @param maxMembri Il numero massimo di membri consentiti.
     * @throws IllegalArgumentException Se l'utente ha già un team, il nome esiste o maxMembri non è valido.
     */
    public void creaTeam(@NonNull Utente utente, @NonNull String nome, int maxMembri) {
        if(utente.haTeam()) {
            throw new IllegalArgumentException("L'utente ha già un team");
        }
        if(getTeam(nome) != null) {
            throw new IllegalArgumentException("Esiste già un team con lo stesso nome");
        }
        Team team = new Team(nome, maxMembri, new HashSet<>(Set.of(utente)));
        utente.setTeam(team);
        addTeam(team);
    }

    /**
     * Gestisce l'uscita di un utente dal proprio team attuale.
     * Se dopo l'uscita il team rimane senza membri, viene rimosso dal sistema.
     *
     * @param utente L'utente che intende uscire dal team.
     * @throws IllegalArgumentException Se l'utente non appartiene a nessun team.
     */
    public void esciDalTeam(@NonNull Utente utente) {
        if(!utente.haTeam()) {
            throw new IllegalArgumentException("L'Utente non ha team!");
        }
        Team team = utente.getTeam();
        utente.setTeam(null);
        team.getMembri().remove(utente);
        if(team.getMembri().isEmpty()) {
            eventPublisher.publishDeletion(team);
            removeTeam(team);
        }
    }

}