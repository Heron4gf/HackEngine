package it.unicam.ids2026.core.managers;

import it.unicam.ids2026.api.events.DeletionListener;
import it.unicam.ids2026.core.roles.team.Invito;
import it.unicam.ids2026.core.roles.team.Team;
import it.unicam.ids2026.core.roles.team.Utente;
import lombok.NonNull;

import java.util.Collection;
import java.util.HashSet;
import java.util.stream.Collectors;

public class InviteManager implements DeletionListener {

    private Collection<Invito> inviti = new HashSet<>();

    /**
     * Invia una richiesta di partecipazione da un team a un utente specifico.
     * L'invito viene aggiunto alla casella degli inviti del destinatario.
     *
     * @param mittente    Il team che invia la richiesta.
     * @param destinatario L'utente che riceve l'invito.
     */
    public void invitaUtente(@NonNull Team mittente, @NonNull Utente destinatario) {
        verifyTeamAndUtente(mittente, destinatario);
        inviti.add(new Invito(mittente, destinatario));
    }

    public Collection<Invito> getCasellaInviti(@NonNull Utente utente) {
        return inviti.stream()
                .filter(invito -> invito.getDestinatario().equals(utente))
                .collect(Collectors.toSet());
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
        removeInvito(invito);
    }

    /**
     * Declina un invito esistente rimuovendolo dalla lista degli inviti dell'utente.
     * Nessuna modifica viene apportata alla composizione del team.
     *
     * @param invito L'invito da rifiutare ed eliminare.
     */
    public void rifiutaInvito(@NonNull Invito invito) {
        removeInvito(invito);
    }



    private void removeInvito(Invito invito) {
        inviti.remove(invito);
    }

    private void verifyTeamAndUtente(Team team, Utente utente) {
        verifyUserTeam(utente);
        verifyTeamSize(team);
    }

    private void verifyTeamSize(Team team) {
        if(team.getMembri().size() >= team.getMaxMembri()) {
            throw new IllegalArgumentException("Team al completo");
        }
    }

    private void verifyUserTeam(Utente utente) {
        if(utente.haTeam()) {
            throw new IllegalArgumentException("L'utente ha già un team");
        }
    }

    /*
    FIXME: Stabilire se Invite Manager deve essere il listener o se delegare l'event handling
        ad una classe apposita.
     */
    @Override
    public void notifyDeletion(Team team) {
        inviti.stream().filter(i -> i.getMittente().equals(team))
                .forEach(this::removeInvito);
    }
}
