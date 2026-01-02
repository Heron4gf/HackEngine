package it.unicam.ids2026.hackhub.managers;

import it.unicam.ids2026.hackhub.Hackathon;
import it.unicam.ids2026.hackhub.data.DatiHackathon;
import it.unicam.ids2026.hackhub.data.Intervallo;
import it.unicam.ids2026.hackhub.roles.Giudice;
import it.unicam.ids2026.hackhub.roles.Mentore;
import it.unicam.ids2026.hackhub.roles.Organizzatore;

import java.util.Collection;
import java.util.LinkedList;
import java.util.UUID;

public class HackathonManager {

    private Collection<Hackathon> hackathons;

    public Hackathon getHackathon(UUID id) {
        return hackathons.stream().filter(h -> h.getId().equals(id)).findFirst().orElse(null);
    }

    public Hackathon creaHackathon(Organizzatore organizzatore, DatiHackathon datiHackathon, Giudice giudice, Intervallo periodoIscrizioni, Intervallo durataHackathon) throws Exception {
        Hackathon newHackathon = new Hackathon(organizzatore, datiHackathon, new LinkedList<>(), giudice, periodoIscrizioni, durataHackathon);
        for (Hackathon h: hackathons) {
            if (newHackathon.equals(h)) {
                throw new Exception("Hackathon already exists");
            }
        }
        hackathons.add(newHackathon);
        return newHackathon;
    }

    public void aggiungiMentori(Hackathon h, Collection<Mentore> mentori) {
        for (Mentore m: mentori) {
            h.aggiungiMentore(m);
        }
    }
}
