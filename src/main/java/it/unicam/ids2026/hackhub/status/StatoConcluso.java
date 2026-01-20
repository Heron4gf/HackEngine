package it.unicam.ids2026.hackhub.status;

import it.unicam.ids2026.hackhub.Hackathon;
import it.unicam.ids2026.hackhub.data.Sottomissione;
import it.unicam.ids2026.hackhub.roles.Mentore;
import it.unicam.ids2026.hackhub.roles.Team;

public class StatoConcluso implements StatoHackathon {
    @Override
    public void next(Hackathon hackathon) {
        throw new IllegalStateException("L'Hackathon è concluso");
    }

    @Override
    public void iscriviTeam(Hackathon hackathon, Team team) {

    }

    @Override
    public void aggiungiMentore(Hackathon hackathon, Mentore mentore) {
        throw new IllegalStateException("Impossibile aggiungere membri l'Hackathon è concluso");
    }

    @Override
    public void aggiungiSottomissione(Hackathon hackathon, Sottomissione sottomissione) {
        throw new IllegalStateException("Impossibile sottomettere progetti l'Hackathon è concluso");
    }
}
