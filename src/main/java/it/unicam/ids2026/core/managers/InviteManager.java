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

@Service
public class InviteManager implements DeletionListener {

    private final InviteRepository inviteRepository;

    @Autowired
    public InviteManager(InviteRepository inviteRepository, EventPublisher eventPublisher) {
        this.inviteRepository = inviteRepository;
        eventPublisher.registerListener(this);
    }

    public void invitaUtente(@NonNull Team mittente, @NonNull Utente destinatario) {
        verifyTeamAndUtente(mittente, destinatario);
        inviteRepository.save(new Invito(mittente, destinatario));
    }

    public Set<Invito> getCasellaInviti(@NonNull Utente utente) {
        return inviteRepository.findByDestinatario(utente);
    }

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
        // TODO: usare Jakarta validator
    }

    private void verifyUserTeam(Utente utente) {
        if (utente.haTeam()) {
            throw new IllegalArgumentException("L'utente ha gia un team");
        }
    }

    @Override
    public void notifyDeletion(Team team) {
        inviteRepository.deleteByMittente(team);
    }
}
