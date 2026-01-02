package it.unicam.ids2026.hackhub;

import it.unicam.ids2026.hackhub.data.DatiHackathon;
import it.unicam.ids2026.hackhub.data.Intervallo;
import it.unicam.ids2026.hackhub.managers.HackathonManager;
import it.unicam.ids2026.hackhub.managers.MentorManager;
import it.unicam.ids2026.hackhub.roles.Giudice;
import it.unicam.ids2026.hackhub.roles.Mentore;
import it.unicam.ids2026.hackhub.roles.Organizzatore;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

@NoArgsConstructor(access = AccessLevel.PRIVATE) // using Singleton
public class HackHub {

    @Getter(lazy = true) // Thread-safe implementation using Lombok
    private static final HackHub instance = new HackHub();

    private final HackathonManager hackathonManager = new HackathonManager();
    private final MentorManager mentorManager = new MentorManager(List.of() /*mock list as placeholder*/);

    public Collection<Mentore> getMentoriDisponibili(Hackathon hackathon) {
        return mentorManager.getMentoriDisponibili(hackathon);
    }

    public Hackathon getHackathon(UUID id) {
        return hackathonManager.getHackathon(id);
    }

    public void aggiungiMentori(Hackathon hackathon, Collection<Mentore> mentori) {
        hackathonManager.aggiungiMentori(hackathon, mentori);
    }

    public Hackathon creaHackathon(Organizzatore organizzatore, DatiHackathon datiHackathon, Giudice giudice, Intervallo periodoIscrizioni, Intervallo durataHackathon) throws Exception {
        return hackathonManager.creaHackathon(organizzatore, datiHackathon, giudice, periodoIscrizioni, durataHackathon);
    }

}
