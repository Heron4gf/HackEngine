package it.unicam.ids2026.core;

import it.unicam.ids2026.api.external.DefaultCalendarWrapper;
import it.unicam.ids2026.core.events.EventPublisher;
import it.unicam.ids2026.core.managers.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.*;

/**
 * Classe principale (Singleton) che funge da registry per i manager del sistema HackHub.
 */
@NoArgsConstructor
@AllArgsConstructor
public class HackHub {

    private static HackHub instance;

    @Getter private final HackathonManager hackathonManager = new HackathonManager(new HashSet<>());
    @Getter private final StaffManager staffManager = new StaffManager(this.userManager);
    @Getter private final InviteManager inviteManager = new InviteManager();
    @Getter private final TeamManager teamManager = new TeamManager(new HashSet<>(), new EventPublisher());
    @Getter private final UserManager userManager = new UserManager(new HashSet<>());
    @Getter private final SubmissionManager submissionManager = new SubmissionManager();
    @Getter private final SupportRequestManager supportRequestManager = new SupportRequestManager(new DefaultCalendarWrapper());


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