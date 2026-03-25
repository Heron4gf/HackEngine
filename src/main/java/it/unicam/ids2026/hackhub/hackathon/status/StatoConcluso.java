package it.unicam.ids2026.hackhub.hackathon.status;

import it.unicam.ids2026.hackhub.hackathon.Hackathon;
import it.unicam.ids2026.hackhub.hackathon.data.Sottomissione;
import it.unicam.ids2026.hackhub.roles.staff.Mentore;
import it.unicam.ids2026.hackhub.roles.team.Team;

public class StatoConcluso implements StatoHackathon {
    @Override
    public void next(Hackathon hackathon) {
        throw new IllegalStateException("L'Hackathon è concluso");
    }

    @Override
    public void iscriviTeam(Hackathon hackathon, Team team) {
        throw new IllegalStateException("L'Hackathon è concluso, impossibile iscriversi");
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
