package it.unicam.ids2026.hackhub.managers;

import it.unicam.ids2026.hackhub.roles.team.Invito;
import it.unicam.ids2026.hackhub.roles.team.Team;
import it.unicam.ids2026.hackhub.roles.team.Utente;
import lombok.NonNull;

public class InviteManager {

    public void invitaUtente(@NonNull Team mittente, @NonNull Utente destinatario) {
        verifyTeamAndUtente(mittente, destinatario);
        destinatario.getCasellaInviti()
                .add(new Invito(mittente, destinatario));
    }

    public void accettaInvito(@NonNull Invito invito) {
        Team mittente = invito.getMittente();
        Utente destinatario = invito.getDestinatario();
        verifyTeamAndUtente(mittente, destinatario);

        mittente.getMembri().add(destinatario);
        destinatario.setTeam(mittente);
        removeInvito(invito);
    }

    public void rifiutaInvito(@NonNull Invito invito) {
        removeInvito(invito);
    }

    private void removeInvito(Invito invito) {
        invito.getDestinatario().getCasellaInviti().remove(invito);
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

}
