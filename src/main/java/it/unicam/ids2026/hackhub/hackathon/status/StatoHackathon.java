package it.unicam.ids2026.hackhub.hackathon.status;

import it.unicam.ids2026.hackhub.hackathon.Hackathon;
import it.unicam.ids2026.hackhub.hackathon.data.Sottomissione;
import it.unicam.ids2026.hackhub.roles.staff.Mentore;
import it.unicam.ids2026.hackhub.roles.team.Team;

public interface StatoHackathon {
    void next(Hackathon hackathon);
    void iscriviTeam(Hackathon hackathon, Team team);
    void aggiungiMentore(Hackathon hackathon, Mentore mentore);
    void aggiungiSottomissione(Hackathon hackathon, Sottomissione sottomissione);
}
