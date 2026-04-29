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
    public Set<Mentore> ottieniMentoriDisponibili(@NonNull Hackathon h) {
        return getMentori().stream()
                .filter(m -> !h.getMentori().contains(m))
                .collect(Collectors.toSet());
    }

    /**
     * Aggiunge i mentori selezionati all'hackathon previa verifica della loro disponibilità.
     *
     * @param hackathon l'hackathon a cui aggiungere i mentori.
     * @param mentoriScelti l'insieme dei mentori da aggiungere.
     * @throws IllegalArgumentException se uno o più mentori selezionati non risultano disponibili.
     */
    public void aggiungiMentori(@NonNull Hackathon hackathon, @NonNull Set<Mentore> mentoriScelti) {
        if (!ottieniMentoriDisponibili(hackathon).containsAll(mentoriScelti)) {
            throw new IllegalArgumentException("Mentori invalidi");
        }

        mentoriScelti.stream().forEach(hackathon::aggiungiMentore);
    }
}
