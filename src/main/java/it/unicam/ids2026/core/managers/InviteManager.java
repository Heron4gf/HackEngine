package it.unicam.ids2026.core.managers;

import it.unicam.ids2026.api.events.DeletionListener;
import it.unicam.ids2026.core.events.EventPublisher;
import it.unicam.ids2026.core.roles.team.Invito;
import it.unicam.ids2026.core.roles.team.Team;
import it.unicam.ids2026.core.roles.team.Utente;
import it.unicam.ids2026.persistence.InviteRepository;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Set;

/**
 * Gestisce le operazioni relative agli inviti tra team e utenti.
 */
@Service
public class InviteManager implements DeletionListener {

    private final InviteRepository inviteRepository;
    private final EventPublisher eventPublisher;

    @Autowired
    public InviteManager(InviteRepository inviteRepository, EventPublisher eventPublisher) {
        this.inviteRepository = inviteRepository;
        eventPublisher.registerListener(this);
        this.eventPublisher = eventPublisher;
    }

    /**
     * Invia un invito a un utente per entrare nel team.
     *
     * @param mittente team che invia l'invito
     * @param destinatario utente che riceve l'invito
     * @throws IllegalArgumentException se l'utente ha già un team
     */
    public void invitaUtente(@NonNull Team mittente, @NonNull Utente destinatario) {
        verifyTeamAndUtente(mittente, destinatario);
        inviteRepository.save(new Invito(mittente, destinatario));
    }

    /**
     * Restituisce la casella degli inviti di un utente.
     *
     * @param utente utente di cui recuperare gli inviti
     * @return insieme di inviti ricevuti dall'utente
     */
    public Set<Invito> getCasellaInviti(@NonNull Utente utente) {
        return inviteRepository.findByDestinatario(utente);
    }

    /**
     * Accetta un invito: l'utente viene aggiunto al team del mittente.
     *
     * @param invito invito da accettare
     * @throws IllegalArgumentException se l'utente ha già un team
     */
    public void accettaInvito(@NonNull Invito invito) {
        Team mittente = invito.getMittente();
        Utente destinatario = invito.getDestinatario();
        verifyTeamAndUtente(mittente, destinatario);

        mittente.getMembri().add(destinatario);
        eventPublisher.publishEnter(mittente, destinatario);
        removeInvito(destinatario, invito);
    }

    /**
     * Trova un invito specifico tra un team e un utente.
     *
     * @param team team mittente dell'invito
     * @param destinatario utente destinatario dell'invito
     * @return l'invito se esiste, null altrimenti
     */
    public Invito findInvito(@NonNull Team team, @NonNull Utente destinatario) {
        return getCasellaInviti(destinatario).stream()
                .filter(invito -> invito.getMittente().equals(team))
                .findFirst()
                .orElse(null);
    }

    /**
     * Rifiuta un invito.
     *
     * @param invito invito da rifiutare
     */
    public void rifiutaInvito(@NonNull Invito invito) {
        removeInvito(invito.getDestinatario(), invito);
    }

    private void removeInvito(Utente destinatario, Invito invito) {
        inviteRepository.delete(destinatario, invito);
    }

    private void verifyTeamAndUtente(Team team, Utente utente) {
        verifyUserTeam(utente);
        verifyTeamSize(team);
    }

    private void verifyTeamSize(Team team) {
        if(team.getMembri().size() >= team.getMaxMembri()) {
            throw new IllegalArgumentException("Il team ha raggiunto il numero massimo di membri");
        }
    }

    private void verifyUserTeam(Utente utente) {
        if (utente.haTeam()) {
            throw new IllegalArgumentException("L'utente ha gia un team");
        }
    }

    /**
     * Gestisce l'eliminazione di un team eliminando tutti gli inviti associati.
     *
     * @param team team eliminato
     */
    @Override
    public void notifyDeletion(Team team) {
        inviteRepository.deleteByMittente(team);
    }
}
