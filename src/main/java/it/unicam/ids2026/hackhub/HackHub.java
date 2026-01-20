package it.unicam.ids2026.hackhub;

import it.unicam.ids2026.hackhub.hackathon.data.DatiHackathon;
import it.unicam.ids2026.hackhub.hackathon.Hackathon;
import it.unicam.ids2026.hackhub.hackathon.data.Intervallo;
import it.unicam.ids2026.hackhub.managers.HackathonManager;
import it.unicam.ids2026.hackhub.managers.MentorManager;
import it.unicam.ids2026.hackhub.managers.TeamManager;
import it.unicam.ids2026.hackhub.managers.UserManager;
import it.unicam.ids2026.hackhub.roles.staff.Giudice;
import it.unicam.ids2026.hackhub.roles.staff.Mentore;
import it.unicam.ids2026.hackhub.roles.staff.Organizzatore;
import it.unicam.ids2026.hackhub.roles.team.Team;
import it.unicam.ids2026.hackhub.roles.team.Utente;
import lombok.NonNull;

import java.util.*;

/**
 * Classe principale (Singleton) che funge da facciata per il sistema HackHub.
 * Coordina le operazioni delegando le responsabilità ai manager specifici.
 */
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

    /**
     * Restituisce l'istanza unica della classe HackHub.
     * Se non esiste, ne crea una nuova in modo sincronizzato.
     *
     * @return L'istanza Singleton di HackHub.
     */
    public static synchronized HackHub getInstance() {
        if (instance == null) {
            instance = new HackHub();
        }
        return instance;
    }

    /**
     * Recupera la collezione di mentori disponibili per un dato hackathon.
     *
     * @param hackathon L'hackathon di riferimento.
     * @return Una collezione di oggetti Mentore.
     */
    public Collection<Mentore> getMentoriDisponibili(Hackathon hackathon) {
        return mentorManager.getMentoriDisponibili(hackathon);
    }

    /**
     * Cerca e restituisce un hackathon tramite il suo identificativo univoco.
     *
     * @param id L'UUID dell'hackathon da cercare.
     * @return L'oggetto Hackathon corrispondente.
     */
    public Hackathon getHackathon(@NonNull UUID id) {
        return hackathonManager.getHackathon(id);
    }

    /**
     * Aggiunge una lista di mentori a un determinato hackathon.
     *
     * @param hackathon L'hackathon a cui aggiungere i mentori.
     * @param mentori La lista di mentori da aggiungere.
     */
    public void aggiungiMentori(@NonNull Hackathon hackathon, @NonNull List<Mentore> mentori) {
        hackathonManager.aggiungiMentori(hackathon, mentori);
    }

    /**
     * Crea un nuovo hackathon e lo registra nel sistema.
     *
     * @param organizzatore L'organizzatore che crea l'evento.
     * @param datiHackathon I dati descrittivi dell'evento.
     * @param giudice Il giudice principale assegnato.
     * @param periodoIscrizioni L'intervallo temporale per le iscrizioni.
     * @param durataHackathon L'intervallo temporale di svolgimento dell'evento.
     * @return L'oggetto Hackathon appena creato.
     */
    public Hackathon creaHackathon(@NonNull Organizzatore organizzatore, @NonNull DatiHackathon datiHackathon, @NonNull Giudice giudice, @NonNull Intervallo periodoIscrizioni, @NonNull Intervallo durataHackathon) {
        return hackathonManager.creaHackathon(organizzatore, datiHackathon, giudice, periodoIscrizioni, durataHackathon);
    }

    /**
     * Cerca un team tramite il suo nome.
     *
     * @param nome Il nome del team da cercare.
     * @return L'oggetto Team trovato.
     */
    public Team getTeam(@NonNull String nome) {
        return teamManager.getTeam(nome);
    }

    /**
     * Cerca il team di appartenenza di uno specifico utente.
     *
     * @param utente L'utente membro del team.
     * @return Il Team a cui appartiene l'utente.
     */
    public Team getTeam(@NonNull Utente utente) {
        return teamManager.getTeam(utente);
    }

    /**
     * Aggiunge un nuovo team al sistema.
     *
     * @param team Il team da registrare.
     */
    public void addTeam(@NonNull Team team) {
        teamManager.addTeam(team);
    }

    /**
     * Rimuove un team esistente dal sistema.
     *
     * @param team Il team da rimuovere.
     */
    public void removeTeam(@NonNull Team team) {
        teamManager.removeTeam(team);
    }

    /**
     * Iscrive un team a uno specifico hackathon.
     *
     * @param hackathon L'hackathon a cui iscriversi.
     * @param team Il team che richiede l'iscrizione.
     */
    public void iscriviHackathonTeam(@NonNull Hackathon hackathon, @NonNull Team team) {
        hackathonManager.iscrizioneHackathon(hackathon, team);
    }

    /**
     * Restituisce tutti i team attualmente registrati nel sistema.
     *
     * @return Un Set contenente tutti i team.
     */
    public Set<Team> getAllTeams() {
        return teamManager.getTeams();
    }
}