package it.unicam.ids2026.hackhub;

import it.unicam.ids2026.hackhub.data.DatiHackathon;
import it.unicam.ids2026.hackhub.data.Intervallo;
import it.unicam.ids2026.hackhub.managers.HackathonManager;
import it.unicam.ids2026.hackhub.managers.MentorManager;
import it.unicam.ids2026.hackhub.roles.Giudice;
import it.unicam.ids2026.hackhub.roles.Mentore;
import it.unicam.ids2026.hackhub.roles.Organizzatore;
import lombok.NonNull;

import java.util.Collection;
import java.util.UUID;

public class HackHub {

    private static HackHub instance;
    private final HackathonManager hackathonManager;
    private final MentorManager mentorManager;

    private HackHub(HackathonManager hackathonManager, MentorManager mentorManager) {
        this.hackathonManager = hackathonManager;
        this.mentorManager = mentorManager;
    }

    public static synchronized HackHub getInstance() {
        if (instance == null) {
            throw new IllegalStateException();
        }
        return instance;
    }

    public static class Builder {
        private HackathonManager hackathonManager;
        private MentorManager mentorManager;

        public Builder withHackathonManager(@NonNull HackathonManager hackathonManager) {
            this.hackathonManager = hackathonManager;
            return this;
        }

        public Builder withMentorManager(@NonNull MentorManager mentorManager) {
            this.mentorManager = mentorManager;
            return this;
        }

        public void build() {
            if (HackHub.instance == null) {
                HackHub.instance = new HackHub(this.hackathonManager, this.mentorManager);
            }
        }
    }

    public Collection<Mentore> getMentoriDisponibili(@NonNull Hackathon hackathon) {
        return mentorManager.getMentoriDisponibili(hackathon);
    }

    public Hackathon getHackathon(@NonNull UUID id) {
        return hackathonManager.getHackathon(id);
    }

    public void aggiungiMentori(@NonNull Hackathon hackathon, @NonNull Collection<Mentore> mentori) {
        hackathonManager.aggiungiMentori(hackathon, mentori);
    }

    public Hackathon creaHackathon(@NonNull Organizzatore organizzatore, @NonNull DatiHackathon datiHackathon, @NonNull Giudice giudice, @NonNull Intervallo periodoIscrizioni, @NonNull Intervallo durataHackathon) throws Exception {
        return hackathonManager.creaHackathon(organizzatore, datiHackathon, giudice, periodoIscrizioni, durataHackathon);
    }
}