package it.unicam.ids2026.core.hackathon.status;

import it.unicam.ids2026.core.hackathon.Hackathon;
import it.unicam.ids2026.core.hackathon.data.Sottomissione;
import it.unicam.ids2026.core.roles.staff.Mentore;
import it.unicam.ids2026.core.roles.team.Team;
import it.unicam.ids2026.core.supportRequest.RichiestaSupporto;

import java.util.stream.Collectors;

public class StatoInValutazione implements StatoHackathon {
    @Override
    public void next(Hackathon hackathon) {
        if(controllaSeValutate(hackathon)) {
            hackathon.setState(new StatoConcluso());
        }
        else {
            throw new UnsupportedOperationException("Impossibile concludere l'hackathon se esiste almeno una sottomissione " +
                    "non valutata");
        }
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
        if (controllaSeValutate(hackathon)) {
            hackathon.setVincitore(team);
        }
        else {
            throw new UnsupportedOperationException("Impossibile assegnare un vincitore se non tutte le sottomissioni" +
                    "sono " +
                    "state valutate");
        }
    }

    private boolean controllaSeValutate(Hackathon hackathon) {
        return
                hackathon.getIscritti().
                        entrySet().
                        stream().
                        filter(entry -> entry.
                                getValue().
                                getSottomissione().
                                getValutazione() == null)
                        .collect(Collectors.toSet()).isEmpty();
    }
}
