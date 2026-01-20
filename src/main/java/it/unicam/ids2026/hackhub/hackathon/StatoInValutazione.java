package it.unicam.ids2026.hackhub.hackathon;

import it.unicam.ids2026.hackhub.hackathon.data.Sottomissione;
import it.unicam.ids2026.hackhub.roles.staff.Mentore;
import it.unicam.ids2026.hackhub.roles.team.Team;

public class StatoInValutazione implements StatoHackathon {
    @Override
    public void next(Hackathon hackathon) {

            hackathon.setState(new StatoConcluso());

    }

    @Override
    public void iscriviTeam(Hackathon hackathon, Team team) {
        throw new RuntimeException("L'Hackathon è in valutazione, impossibile iscriversi");
    }

    @Override
    public void aggiungiMentore(Hackathon hackathon, Mentore mentore) {
        throw new IllegalStateException("Impossibile aggiungere membri durante la fase di valutazione");
    }

    @Override
    public void aggiungiSottomissione(Hackathon hackathon, Sottomissione sottomissione) {
        throw new IllegalStateException("Impossibile sottomettere progetti durante la fase di iscrizione");
    }
}
