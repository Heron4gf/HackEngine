package it.unicam.ids2026.hackhub;

import it.unicam.ids2026.hackhub.managers.*;

import java.util.*;

/**
 * Classe principale (Singleton) che funge da registry per i manager del sistema HackHub.
 */
public class HackHub {

    private static HackHub instance;

    private final HackathonManager hackathonManager;
    private final StaffManager staffManager;
    private final InviteManager inviteManager;
    private final TeamManager teamManager;
    private final UserManager userManager;

    private HackHub() {
        this.hackathonManager = new HackathonManager(new HashSet<>());
        this.userManager = new UserManager(new HashSet<>());
        this.staffManager = new StaffManager(this.userManager);
        this.inviteManager = new InviteManager();
        this.teamManager = new TeamManager(new HashSet<>());
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
     * Restituisce il gestore responsabile delle operazioni sugli hackathon,
     * come creazione, avanzamento stati e gestione iscrizioni.
     *
     * @return L'istanza di HackathonManager.
     */
    public HackathonManager getHackathonManager() {
        return hackathonManager;
    }

    /**
     * Restituisce il gestore responsabile della registrazione e assegnazione dei mentori.
     *
     * @return L'istanza di StaffManager.
     */
    public StaffManager getStaffManager() {
        return staffManager;
    }

    /**
     * Restituisce il gestore responsabile del flusso degli inviti tra utenti e team.
     *
     * @return L'istanza di InviteManager.
     */
    public InviteManager getInviteManager() {
        return inviteManager;
    }

    /**
     * Restituisce il gestore responsabile della creazione, modifica e rimozione dei team.
     *
     * @return L'istanza di TeamManager.
     */
    public TeamManager getTeamManager() {
        return teamManager;
    }

    /**
     * Restituisce il gestore responsabile dell'anagrafica e del recupero degli utenti.
     *
     * @return L'istanza di UserManager.
     */
    public UserManager getUserManager() {
        return userManager;
    }
}