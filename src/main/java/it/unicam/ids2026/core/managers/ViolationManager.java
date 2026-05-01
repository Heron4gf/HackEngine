package it.unicam.ids2026.core.managers;

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
    private final HackathonRepository hackathonRepository;

    /**
     * Segnala un team per una violazione.
     *
     * @param hackathon l'hackathon di riferimento
     * @param team il team che ha commesso la violazione
     * @param mentore il mentore che segnala la violazione
     * @param descrizione la descrizione della violazione
     */
    public void segnalaTeam(@NonNull Hackathon hackathon, @NonNull Team team, @NonNull Mentore mentore, @NonNull String descrizione) {
        hackathonRepository.findById(hackathon.getId()).ifPresent(
                result -> {
                    result.getViolazioni().add(
                            new Violazione(
                                    mentore,
                                    team,
                                    hackathon,
                                    descrizione
                            )
                    );
                    hackathonRepository.save(result);
                }
        );
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

}
