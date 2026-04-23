package it.unicam.ids2026.core.hackathon.status;

import it.unicam.ids2026.core.hackathon.Hackathon;
import it.unicam.ids2026.core.hackathon.data.Sottomissione;
import it.unicam.ids2026.core.roles.staff.Mentore;
import it.unicam.ids2026.core.roles.team.Team;
import it.unicam.ids2026.core.supportRequest.RichiestaSupporto;

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

    @Override
    public void aggiungiRichiestaSupporto(Hackathon hackathon, RichiestaSupporto richiestaSupporto) {
        throw new IllegalStateException("Impossibile avanzare richieste di supporto ad hackathon concluso");
    }

    @Override
    public RappresentazioneStato getRappresentazioneStato() {
        return RappresentazioneStato.CONCLUSO;
    }
}
