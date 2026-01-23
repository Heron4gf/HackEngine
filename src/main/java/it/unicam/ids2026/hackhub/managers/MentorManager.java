package it.unicam.ids2026.hackhub.managers;

import it.unicam.ids2026.hackhub.hackathon.Hackathon;
import it.unicam.ids2026.hackhub.roles.staff.Mentore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;

import java.util.Collection;
import java.util.stream.Collectors;

@AllArgsConstructor
@Getter
public class MentorManager {
    private Collection<Mentore> listaMentori;

    /**
     * Registra un nuovo mentore nel sistema aggiungendolo alla lista gestita.
     *
     * @param mentore Il mentore da registrare.
     */
    public void registraMentore(@NonNull Mentore mentore) {
        this.listaMentori.add(mentore);
    }

    /**
     * Restituisce una collezione di mentori che non sono ancora associati allo specifico Hackathon.
     * Utile per individuare i mentori liberi per l'assegnazione.
     *
     * @param h L'Hackathon di riferimento per verificare la disponibilità.
     * @return Una collezione di mentori non presenti nell'Hackathon specificato.
     */
    public Collection<Mentore> getMentoriDisponibili(@NonNull Hackathon h) {
        return listaMentori.stream()
                .filter(m -> !h.getMentori().contains(m))
                .collect(Collectors.toList());
    }
}