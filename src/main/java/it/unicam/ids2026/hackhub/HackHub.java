package it.unicam.ids2026.hackhub;

import it.unicam.ids2026.hackhub.data.DatiHackathon;
import it.unicam.ids2026.hackhub.data.Intervallo;
import it.unicam.ids2026.hackhub.managers.HackathonManager;
import it.unicam.ids2026.hackhub.managers.MentorManager;
import it.unicam.ids2026.hackhub.managers.TeamManager;
import it.unicam.ids2026.hackhub.managers.UserManager;
import it.unicam.ids2026.hackhub.roles.Giudice;
import it.unicam.ids2026.hackhub.roles.Mentore;
import it.unicam.ids2026.hackhub.roles.Organizzatore;
import it.unicam.ids2026.hackhub.roles.Team;
import it.unicam.ids2026.hackhub.roles.Utente;
import lombok.NonNull;

import java.util.*;

public class HackHub {

    private static HackHub instance;

    private final HackathonManager hackathonManager;
    private final MentorManager mentorManager;
    private final TeamManager teamManager;
    private final UserManager userManager;

    private HackHub() {
        this.hackathonManager = new HackathonManager(new LinkedList<>());
        this.mentorManager = new MentorManager(new HashSet<>());
        this.teamManager = new TeamManager(new HashSet<>());
        this.userManager = new UserManager(new HashSet<>());
    }

    public static synchronized HackHub getInstance() {
        if (instance == null) {
            instance = new HackHub();
        }
        return instance;
    }

    public Collection<Mentore> getMentoriDisponibili(Hackathon hackathon) {
        return mentorManager.getMentoriDisponibili(hackathon);
    }

    public Hackathon getHackathon(@NonNull UUID id) {
        return hackathonManager.getHackathon(id);
    }

    public void aggiungiMentori(@NonNull Hackathon hackathon, @NonNull List<Mentore> mentori) {
        hackathonManager.aggiungiMentori(hackathon, mentori);
    }

    public Hackathon creaHackathon(@NonNull Organizzatore organizzatore, @NonNull DatiHackathon datiHackathon, @NonNull Giudice giudice, @NonNull Intervallo periodoIscrizioni, @NonNull Intervallo durataHackathon) {
        return hackathonManager.creaHackathon(organizzatore, datiHackathon, giudice, periodoIscrizioni, durataHackathon);
    }

    public Team getTeam(@NonNull String nome) {
        return teamManager.getTeam(nome);
    }

    public Team getTeam(@NonNull Utente utente) {
        return teamManager.getTeam(utente);
    }

    public void addTeam(@NonNull Team team) {
        teamManager.addTeam(team);
    }

    public void removeTeam(@NonNull Team team) {
        teamManager.removeTeam(team);
    }

    public void iscriviHackathonTeam(@NonNull Hackathon hackathon, @NonNull Team team) {
        hackathonManager.iscrizioneHackathon(hackathon, team);
    }
}