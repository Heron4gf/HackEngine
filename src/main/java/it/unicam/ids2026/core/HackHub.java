package it.unicam.ids2026.core;

import it.unicam.ids2026.core.events.EventPublisher;
import it.unicam.ids2026.core.managers.*;
import lombok.Getter;

import java.util.*;

/**
 * Classe principale (Singleton) che funge da registry per i manager del sistema HackHub.
 */
public class HackHub {

    private static HackHub instance;

    @Getter private final HackathonManager hackathonManager;
    @Getter private final StaffManager staffManager;
    @Getter private final InviteManager inviteManager;
    @Getter private final TeamManager teamManager;
    @Getter private final UserManager userManager;

    private HackHub() {
        this.hackathonManager = new HackathonManager(new HashSet<>());
        this.userManager = new UserManager(new HashSet<>());
        this.staffManager = new StaffManager(this.userManager);
        this.inviteManager = new InviteManager();
        this.teamManager = new TeamManager(new HashSet<>(), new EventPublisher());
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
}