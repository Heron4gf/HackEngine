package it.unicam.ids2026.core.managers;

import it.unicam.ids2026.core.hackathon.Hackathon;
import it.unicam.ids2026.core.roles.staff.Giudice;
import it.unicam.ids2026.core.roles.staff.Mentore;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;

/**
 * Gestisce le operazioni relative allo staff degli hackathon.
 */
@Service
public class StaffManager {

    private final UserManager userManager;

    @Autowired
    public StaffManager(UserManager userManager) {
        this.userManager = userManager;
    }

    /**
     * Restituisce tutti i giudici presenti nel sistema.
     *
     * @return insieme di tutti i giudici
     */
    public Set<Giudice> getGiudici() {
        return userManager.getUsersByRole(Giudice.class);
    }

    /**
     * Restituisce tutti i mentori presenti nel sistema.
     *
     * @return insieme di tutti i mentori
     */
    public Set<Mentore> getMentori() {
        return userManager.getUsersByRole(Mentore.class);
    }

    /**
     * Restituisce una collezione di mentori che non sono ancora associati allo specifico Hackathon.
     * Utile per individuare i mentori liberi per l'assegnazione.
     *
     * @param h Hackathon di riferimento per verificare la disponibilità
     * @return una collezione di mentori non presenti nell'Hackathon specificato
     */
    public Set<Mentore> getMentoriDisponibili(@NonNull Hackathon h) {
        return getMentori().stream()
                .filter(m -> !h.getMentori().contains(m))
                .collect(Collectors.toSet());
    }
}
