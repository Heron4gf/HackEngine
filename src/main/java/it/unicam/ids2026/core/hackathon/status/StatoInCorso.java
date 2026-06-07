package it.unicam.ids2026.core.hackathon.status;

import it.unicam.ids2026.core.hackathon.Hackathon;
import it.unicam.ids2026.core.hackathon.data.Sottomissione;
import it.unicam.ids2026.core.roles.staff.Mentore;
import it.unicam.ids2026.core.roles.team.Team;
import it.unicam.ids2026.core.supportRequest.RichiestaSupporto;
import it.unicam.ids2026.core.supportRequest.StatoRichiesta;

public class StatoInCorso implements StatoHackathon {
    @Override
    public void next(Hackathon hackathon) {
        hackathon.setState(new StatoInValutazione());
    }

    @Override
    public void iscriviTeam(Hackathon hackathon, Team team) {
        throw new IllegalStateException("L'Hackathon è in corso, impossibile iscriversi");
    }

    @Override
    public void aggiungiMentore(Hackathon hackathon, Mentore mentore) {
        hackathon.getMentori().add(mentore);
    }

    @Override
    public void aggiungiSottomissione(Hackathon hackathon, Team team, Sottomissione sottomissione) {
        hackathon.getIscritti().get(team).setSottomissione(sottomissione);
    }

    @Override
    public void aggiungiRichiestaSupporto(Hackathon hackathon, Team team, RichiestaSupporto richiestaSupporto) {
        RichiestaSupporto originalRequest = hackathon.getIscritti().get(team).getRichiestaSupporto();
        if(originalRequest != null && originalRequest.getStato() != StatoRichiesta.CHIUSA) {
            throw new IllegalArgumentException("Il team ha già una richiesta di supporto aperta");
        }
        hackathon.getIscritti().get(team).setRichiestaSupporto(richiestaSupporto);
    }

    @Override
    public RappresentazioneStato getRappresentazioneStato() {
        return RappresentazioneStato.IN_CORSO;
    }

    @Override
    public void assegnaVincitore(Hackathon hackathon, Team team) {
        throw new IllegalStateException("Impossibile assegnare un vincitore quando l'hackathon è in corso");
    }
}