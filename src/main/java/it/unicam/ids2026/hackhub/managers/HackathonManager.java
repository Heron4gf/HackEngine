package it.unicam.ids2026.hackhub.managers;

import it.unicam.ids2026.hackhub.Hackathon;
import it.unicam.ids2026.hackhub.data.DatiHackathon;
import it.unicam.ids2026.hackhub.data.Intervallo;
import it.unicam.ids2026.hackhub.roles.Giudice;
import it.unicam.ids2026.hackhub.roles.Mentore;
import it.unicam.ids2026.hackhub.roles.Organizzatore;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
public class HackathonManager {

    private final List<Hackathon> hackathons;

    public Hackathon getHackathon(UUID id) {
        return hackathons.stream()
                .filter(h -> h.getId().equals(id))
                .findFirst()
                .orElse(null);
    }

    public Hackathon creaHackathon(Organizzatore organizzatore, DatiHackathon dati, Giudice giudice, Intervallo iscrizioni, Intervallo durata) {
        Hackathon newHackathon = new Hackathon(organizzatore, dati, giudice, iscrizioni, durata);
        hackathons.add(newHackathon);
        return newHackathon;
    }

    public void aggiungiMentori(Hackathon h, Collection<Mentore> mentori) {
        for (Mentore m : mentori) {
            h.aggiungiMentore(m);
        }
    }
}