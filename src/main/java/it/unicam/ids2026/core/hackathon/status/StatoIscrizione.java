package it.unicam.ids2026.core.hackathon.status;

import it.unicam.ids2026.core.hackathon.Hackathon;
import it.unicam.ids2026.core.hackathon.data.Sottomissione;
import it.unicam.ids2026.core.roles.staff.Mentore;
import it.unicam.ids2026.core.roles.team.Team;
import it.unicam.ids2026.core.supportRequest.RichiestaSupporto;

public class StatoIscrizione implements StatoHackathon {
    @Override
    public void next(Hackathon hackathon) {
        hackathon.setState(new StatoInCorso());
    }

    @Override
    public void iscriviTeam(Hackathon hackathon, Team team) {
        hackathon.getIscritti().add(team);
    }

    @Override
    public void aggiungiMentore(Hackathon hackathon, Mentore mentore) {
        hackathon.getMentori().add(mentore);
    }

    @Override
    public void aggiungiSottomissione(Hackathon hackathon, Sottomissione sottomissione) {
        throw new IllegalStateException("Impossibile sottomettere progetti durante la fase di iscrizione");
    }

    @Override
    public void aggiungiRichiestaSupporto(Hackathon hackathon, RichiestaSupporto richiestaSupporto) {
        throw new IllegalStateException("Impossibile avanzare richieste di supporto quando l'hackathon è in fase di " +
                "iscrizione");
    }

    @Override
    public RappresentazioneStato getRappresentazioneStato() {
        return RappresentazioneStato.ISCRIZIONE;
    }
}