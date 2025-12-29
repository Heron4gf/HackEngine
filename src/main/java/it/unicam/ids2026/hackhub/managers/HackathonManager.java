package it.unicam.ids2026.hackhub.managers;

import it.unicam.ids2026.hackhub.Hackathon;
import it.unicam.ids2026.hackhub.data.DatiHackathon;
import it.unicam.ids2026.hackhub.roles.Mentore;

import java.util.List;
import java.util.UUID;

public class HackathonManager {

    private List<Hackathon> hackathons;

    public Hackathon getHackathon(UUID id) {
        return hackathons.stream().filter(h -> h.getId().equals(id)).findFirst().orElse(null);
    }

    public Hackathon creaHackathon(DatiHackathon datiHackathon) throws Exception {
        Hackathon newHackathon = new Hackathon();
        for (Hackathon h: hackathons) {
            if (newHackathon.equals(h)) {
                throw new Exception("Hackathon already exists");
            }
        }
        hackathons.add(newHackathon);
        return newHackathon;
    }

    public void aggiungiMentori(Hackathon h, List<Mentore> mentori) {

    }
}
