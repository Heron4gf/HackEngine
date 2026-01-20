package it.unicam.ids2026.hackhub.hackathon;

import it.unicam.ids2026.hackhub.hackathon.data.Sottomissione;
import it.unicam.ids2026.hackhub.roles.Mentore;
import it.unicam.ids2026.hackhub.roles.Team;

public class StatoInCorso implements StatoHackathon {
    @Override
    public void next(Hackathon hackathon) {
        hackathon.setState(new StatoInValutazione());
    }

    @Override
    public void iscriviTeam(Hackathon hackathon, Team team) {
        throw new RuntimeException("L'Hackathon è in corso, impossibile iscriversi");
    }

    @Override
    public void aggiungiMentore(Hackathon hackathon, Mentore mentore) {
        hackathon.doAggiungiMentore(mentore);
    }

    @Override
    public void aggiungiSottomissione(Hackathon hackathon, Sottomissione sottomissione) {
        hackathon.doAggiungiSottomissione(sottomissione);
    }
}