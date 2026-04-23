package it.unicam.ids2026.core.managers;

import it.unicam.ids2026.core.hackathon.Hackathon;
import it.unicam.ids2026.core.roles.staff.Giudice;
import it.unicam.ids2026.core.roles.staff.Mentore;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class StaffManager {

    private UserManager userManager;

    public Set<Giudice> getGiudici() {
        return userManager.getUsersByRole(Giudice.class);
    }

    public Set<Mentore> getMentori() {
        return userManager.getUsersByRole(Mentore.class);
    }

    /**
     * Restituisce una collezione di mentori che non sono ancora associati allo specifico Hackathon.
     * Utile per individuare i mentori liberi per l'assegnazione.
     *
     * @param h L'Hackathon di riferimento per verificare la disponibilità.
     * @return Una collezione di mentori non presenti nell'Hackathon specificato.
     */
    public Set<Mentore> getMentoriDisponibili(@NonNull Hackathon h) {
        return getMentori().stream()
                .filter(m -> !h.getMentori().contains(m))
                .collect(Collectors.toSet());
    }
}