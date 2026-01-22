package it.unicam.ids2026.hackhub.managers;

import it.unicam.ids2026.hackhub.hackathon.Hackathon;
import it.unicam.ids2026.hackhub.hackathon.data.DatiHackathon;
import it.unicam.ids2026.hackhub.hackathon.data.Intervallo;
import it.unicam.ids2026.hackhub.hackathon.data.Sottomissione;
import it.unicam.ids2026.hackhub.roles.staff.Giudice;
import it.unicam.ids2026.hackhub.roles.staff.Mentore;
import it.unicam.ids2026.hackhub.roles.staff.Organizzatore;
import it.unicam.ids2026.hackhub.roles.team.Team;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.util.Collection;
import java.util.Set;
import java.util.UUID;

@RequiredArgsConstructor
public class HackathonManager {

    @Getter
    private final Set<Hackathon> hackathons;

    public Hackathon getHackathon(@NonNull UUID id) {
        return hackathons.stream()
                .filter(h -> h.getId().equals(id))
                .findFirst()
                .orElse(null);
    }

    public Hackathon creaHackathon(@NonNull Organizzatore organizzatore,
                                   @NonNull DatiHackathon dati,
                                   @NonNull Giudice giudice,
                                   @NonNull Intervallo iscrizioni,
                                   @NonNull Intervallo durata) {
        Hackathon newHackathon = new Hackathon(organizzatore, dati, giudice, iscrizioni, durata);
        hackathons.add(newHackathon);
        return newHackathon;
    }

    public void avanzaStato(@NonNull Hackathon hackathon) {
        hackathon.getState().next(hackathon);
    }

    public void aggiungiMentore(@NonNull Hackathon hackathon, @NonNull Mentore mentore) {
        if (hackathon.getMentori().contains(mentore)) {
            throw new IllegalArgumentException("Mentore già presente");
        }
        hackathon.getState().aggiungiMentore(hackathon, mentore);
        hackathon.getMentori().add(mentore);
    }

    public void aggiungiMentori(@NonNull Hackathon hackathon, @NonNull Collection<Mentore> mentori) {
        mentori.forEach(m -> this.aggiungiMentore(hackathon, m));
    }

    public void iscriviTeam(@NonNull Hackathon hackathon, @NonNull Team team) {
        if (hackathon.getIscritti().contains(team)) {
            throw new IllegalArgumentException("Team già iscritto");
        }
        hackathon.getIscritti().add(team);
    }

    public void aggiungiSottomissione(@NonNull Hackathon hackathon, @NonNull Sottomissione sottomissione) {
        hackathon.getState().aggiungiSottomissione(hackathon, sottomissione);
        hackathon.getSottomissioni().add(sottomissione);
    }
}