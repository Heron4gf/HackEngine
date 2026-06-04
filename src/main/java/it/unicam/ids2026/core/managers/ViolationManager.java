package it.unicam.ids2026.core.managers;

import it.unicam.ids2026.core.events.EventPublisher;
import it.unicam.ids2026.core.hackathon.Hackathon;
import it.unicam.ids2026.core.roles.staff.Mentore;
import it.unicam.ids2026.core.roles.team.Team;
import it.unicam.ids2026.core.violation.Violazione;
import it.unicam.ids2026.persistence.HackathonRepository;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Set;

/**
 * Gestisce le violazioni degli hackathon.
 */
@Service
@RequiredArgsConstructor
public class ViolationManager {

    private final EventPublisher eventPublisher;

    /**
     * Segnala un team per una violazione.
     *
     * @param hackathon l'hackathon di riferimento
     * @param team il team che ha commesso la violazione
     * @param mentore il mentore che segnala la violazione
     * @param descrizione la descrizione della violazione
     */
    public Violazione segnalaTeam(@NonNull Hackathon hackathon, @NonNull Team team, @NonNull Mentore mentore, @NonNull String descrizione) {
        if (!validaDescrizione(descrizione)) {
            throw new IllegalArgumentException("La descrizione della violazione non e valida");
        }
        if (!hackathon.getIscritti().containsKey(team)) {
            throw new IllegalArgumentException("Il team non risulta iscritto all'hackathon");
        }
        Violazione violazione = new Violazione(mentore, team, hackathon, descrizione);
        hackathon.getViolazioni().add(violazione);
        eventPublisher.publishHackathonChange(hackathon);
        return violazione;
    }

    /**
     * Restituisce le violazioni di un hackathon.
     *
     * @param hackathon l'hackathon di riferimento
     * @return insieme delle violazioni
     */
    public Set<Violazione> getViolations(@NonNull Hackathon hackathon) {
        return hackathon.getViolazioni();
    }

    private boolean validaDescrizione(String descrizione) {
        return descrizione != null && !descrizione.isBlank();
    }

}
