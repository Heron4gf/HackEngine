package it.unicam.ids2026.core.managers;

import it.unicam.ids2026.api.events.DeletionListener;
import it.unicam.ids2026.core.roles.team.Invito;
import it.unicam.ids2026.core.roles.team.Team;
import it.unicam.ids2026.core.roles.team.Utente;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Service
public class InviteManager implements DeletionListener {

    private final Map<Utente, Set<Invito>> inviti = new HashMap<>();

    @Autowired
    public InviteManager() {
    }

    /**
     * Invia una richiesta di partecipazione da un team a un utente specifico.
     * L'invito viene aggiunto alla casella degli inviti del destinatario.
     *
     * @param mittente     Il team che invia la richiesta.
     * @param destinatario L'utente che riceve l'invito.
     */
    public void invitaUtente(@NonNull Team mittente, @NonNull Utente destinatario) {
        verifyTeamAndUtente(mittente, destinatario);
        inviti.computeIfAbsent(destinatario, k -> new HashSet<>());
        inviti.get(destinatario).add(new Invito(mittente, destinatario));
    }

    public Set<Invito> getCasellaInviti(@NonNull Utente utente) {
        return inviti.getOrDefault(utente, new HashSet<>());
    }

    /**
     * Finalizza l'accettazione di un invito, aggiungendo l'utente ai membri del team.
     * Aggiorna il riferimento del team nell'oggetto utente e rimuove l'invito pendente.
     *
     * @param invito L'invito da processare e accettare.
     */
    public void accettaInvito(@NonNull Invito invito) {
        Team mittente = invito.getMittente();
        Utente destinatario = invito.getDestinatario();
        verifyTeamAndUtente(mittente, destinatario);

        mittente.getMembri().add(destinatario);
        destinatario.setTeam(mittente);
        removeInvito(destinatario, invito);
    }

    public Invito findInvito(@NonNull Team team, @NonNull Utente destinatario) {
        return getCasellaInviti(destinatario).stream()
                .filter(invito -> invito.getMittente().equals(team))
                .findFirst()
                .orElse(null);
    }


    /**
     * Declina un invito esistente rimuovendolo dalla lista degli inviti dell'utente.
     * Nessuna modifica viene apportata alla composizione del team.
     *
     * @param invito L'invito da rifiutare ed eliminare.
     */
    public void rifiutaInvito(@NonNull Invito invito) {
        removeInvito(invito.getDestinatario(), invito);
    }

    private void removeInvito(Utente destinatario, Invito invito) {
        if (inviti.containsKey(destinatario)) {
            inviti.get(destinatario).remove(invito);
        }
    }

    private void verifyTeamAndUtente(Team team, Utente utente) {
        verifyUserTeam(utente);
        verifyTeamSize(team);
    }

    private void verifyTeamSize(Team team) {
        // TODO: usare Jakarta validator
    }

    private void verifyUserTeam(Utente utente) {
        if (utente.haTeam()) {
            throw new IllegalArgumentException("L'utente ha già un team");
        }
    }

    @Override
    public void notifyDeletion(Team team) {
        inviti.values().stream()
                .flatMap(Collection::stream)
                .filter(invito -> invito.getMittente().equals(team))
                .forEach(invito -> removeInvito(invito.getDestinatario(), invito));
    }
}
