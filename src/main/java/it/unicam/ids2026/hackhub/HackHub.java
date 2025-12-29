package it.unicam.ids2026.hackhub;

import it.unicam.ids2026.hackhub.data.DatiHackathon;
import it.unicam.ids2026.hackhub.managers.HackathonManager;
import it.unicam.ids2026.hackhub.managers.MentorManager;
import it.unicam.ids2026.hackhub.roles.Mentore;

import java.util.Collection;
import java.util.UUID;

public class HackHub {

    private static HackHub instance;

    private final HackathonManager hackathonManager = new HackathonManager();
    private final MentorManager mentorManager = new MentorManager();

    private HackHub() {
        // using Singleton
    }

    public static HackHub getInstance() {
        if(instance == null) instance = new HackHub();
        return instance;
    }

    public Collection<Mentore> getMentoriDisponibili() {
        return mentorManager.getMentoriDisponibili();
    }

    public Hackathon getHackathon(UUID id) {
        return hackathonManager.getHackathon(id);
    }

    public void aggiungiMentori(Hackathon hackathon, Collection<Mentore> mentori) {
        mentori.stream()
                .forEach(hackathon::aggiungiMentore);
    }

    public Hackathon creaHackathon(DatiHackathon datiHackathon) throws Exception {
        return hackathonManager.creaHackathon(datiHackathon);
    }

}
