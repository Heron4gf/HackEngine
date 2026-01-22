package it.unicam.ids2026.hackhub;

import it.unicam.ids2026.hackhub.managers.*;

import java.util.*;

/**
 * Classe principale (Singleton) che funge da facciata per il sistema HackHub.
 * Coordina le operazioni delegando le responsabilità ai manager specifici.
 */
public class HackHub {

    private static HackHub instance;

    private final HackathonManager hackathonManager;
    private final MentorManager mentorManager;
    private final InviteManager inviteManager;
    private final TeamManager teamManager;
    private final UserManager userManager;

    private HackHub() {
        this.hackathonManager = new HackathonManager(new HashSet<>());
        this.mentorManager = new MentorManager(new HashSet<>());
        this.inviteManager = new InviteManager();
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

    public HackathonManager getHackathonManager() {
        return hackathonManager;
    }

    public MentorManager getMentorManager() {
        return mentorManager;
    }

    public InviteManager getInviteManager() {
        return inviteManager;
    }

    public TeamManager getTeamManager() {
        return teamManager;
    }

    public UserManager getUserManager() {
        return userManager;
    }
}