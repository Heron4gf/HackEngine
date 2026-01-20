package it.unicam.ids2026.hackhub.status;

import it.unicam.ids2026.hackhub.Hackathon;
import it.unicam.ids2026.hackhub.data.Sottomissione;
import it.unicam.ids2026.hackhub.roles.Mentore;
import it.unicam.ids2026.hackhub.roles.Team;

public class StatoIscrizione implements StatoHackathon {
    @Override
    public void next(Hackathon hackathon) {
        hackathon.setState(new StatoInCorso());
    }

    @Override
    public void iscriviTeam(Hackathon hackathon, Team team) {
    }

    @Override
    public void aggiungiMentore(Hackathon hackathon, Mentore mentore) {
        hackathon.doAggiungiMentore(mentore);
    }

    @Override
    public void aggiungiSottomissione(Hackathon hackathon, Sottomissione sottomissione) {
        throw new IllegalStateException("Impossibile sottomettere progetti durante la fase di iscrizione");
    }
}