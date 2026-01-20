package it.unicam.ids2026.hackhub.status;

import it.unicam.ids2026.hackhub.Hackathon;
import it.unicam.ids2026.hackhub.data.Sottomissione;
import it.unicam.ids2026.hackhub.roles.Mentore;
import it.unicam.ids2026.hackhub.roles.Team;

public class StatoInCorso implements StatoHackathon {
    @Override
    public void next(Hackathon hackathon) {
        hackathon.setState(new StatoInValutazione());
    }

    @Override
    public void iscriviTeam(Hackathon hackathon, Team team) {
    }

    @Override
    public void aggiungiMentore(Hackathon hackathon, Mentore mentore) {
        throw new IllegalStateException("Hackathon in corso: impossibile aggiungere mentori");
    }

    @Override
    public void aggiungiSottomissione(Hackathon hackathon, Sottomissione sottomissione) {
        hackathon.doAggiungiSottomissione(sottomissione);
    }
}