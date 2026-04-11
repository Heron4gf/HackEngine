package it.unicam.ids2026.core.hackathon.status;

import it.unicam.ids2026.core.hackathon.Hackathon;
import it.unicam.ids2026.core.hackathon.data.Sottomissione;
import it.unicam.ids2026.core.roles.staff.Mentore;
import it.unicam.ids2026.core.roles.team.Team;

public interface StatoHackathon {
    void next(Hackathon hackathon);
    void iscriviTeam(Hackathon hackathon, Team team);
    void aggiungiMentore(Hackathon hackathon, Mentore mentore);
    void aggiungiSottomissione(Hackathon hackathon, Sottomissione sottomissione);
    RappresentazioneStato getRappresentazioneStato();
}
