package it.unicam.ids2026.core.hackathon.status;

import it.unicam.ids2026.core.hackathon.Hackathon;
import it.unicam.ids2026.core.hackathon.data.Sottomissione;
import it.unicam.ids2026.core.roles.staff.Mentore;
import it.unicam.ids2026.core.roles.team.Team;
import it.unicam.ids2026.core.supportRequest.RichiestaSupporto;

public class StatoInValutazione implements StatoHackathon {
    @Override
    public void next(Hackathon hackathon) {
        if(hackathon.getVincitore() == null)
            throw new UnsupportedOperationException("Bisogna avere un vincitore per concludere l'Hackathon");
        hackathon.setState(new StatoConcluso());
    }

    @Override
    public void iscriviTeam(Hackathon hackathon, Team team) {
        throw new IllegalStateException("L'Hackathon è in valutazione, impossibile iscriversi");
    }

    @Override
    public void aggiungiMentore(Hackathon hackathon, Mentore mentore) {
        throw new IllegalStateException("Impossibile aggiungere membri durante la fase di valutazione");
    }

    @Override
    public void aggiungiSottomissione(Hackathon hackathon, Team team, Sottomissione sottomissione) {
        throw new IllegalStateException("Impossibile sottomettere progetti durante la fase di valutazione");
    }

    @Override
    public void aggiungiRichiestaSupporto(Hackathon hackathon, Team team, RichiestaSupporto richiestaSupporto) {
        throw new IllegalStateException("Impossibile avanzare richieste di supporto quando l'hackathon è in " +
                "valutazione");
    }


    @Override
    public RappresentazioneStato getRappresentazioneStato() {
        return RappresentazioneStato.VALUTAZIONE;
    }

    @Override
    public void assegnaVincitore(Hackathon hackathon, Team team) {
        hackathon.setVincitore(team);
    }
}
