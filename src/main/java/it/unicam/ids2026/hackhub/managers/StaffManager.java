package it.unicam.ids2026.hackhub.managers;

import it.unicam.ids2026.hackhub.hackathon.Hackathon;
import it.unicam.ids2026.hackhub.roles.staff.Giudice;
import it.unicam.ids2026.hackhub.roles.staff.Mentore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

@AllArgsConstructor
@Getter
public class StaffManager {

    private UserManager userManager;

    public Set<Giudice> getGiudici() {
        return userManager.getUsersByRole(Giudice.class);
    }

    /**
     * Restituisce una collezione di mentori che non sono ancora associati allo specifico Hackathon.
     * Utile per individuare i mentori liberi per l'assegnazione.
     *
     * @param h L'Hackathon di riferimento per verificare la disponibilità.
     * @return Una collezione di mentori non presenti nell'Hackathon specificato.
     */
    public Collection<Mentore> getMentoriDisponibili(@NonNull Hackathon h) {
        return userManager.getUsersByRole(Mentore.class).stream()
                .filter(m -> !h.getMentori().contains(m))
                .collect(Collectors.toList());
    }
}