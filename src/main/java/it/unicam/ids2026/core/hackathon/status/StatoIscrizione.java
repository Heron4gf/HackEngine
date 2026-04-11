package it.unicam.ids2026.core.hackathon.status;

import it.unicam.ids2026.core.HackHub;
import it.unicam.ids2026.core.hackathon.Hackathon;
import it.unicam.ids2026.core.hackathon.data.Sottomissione;
import it.unicam.ids2026.core.roles.staff.Mentore;
import it.unicam.ids2026.core.roles.team.Team;

public class StatoIscrizione implements StatoHackathon {
    @Override
    public void next(Hackathon hackathon) {
        hackathon.setState(new StatoInCorso());
    }

    @Override
    public void iscriviTeam(Hackathon hackathon, Team team) {
        HackHub.getInstance().getHackathonManager().iscriviTeam(hackathon, team);
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
    public RappresentazioneStato getRappresentazioneStato() {
        return RappresentazioneStato.ISCRIZIONE;
    }
}