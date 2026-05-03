package it.unicam.ids2026.core.managers;

import it.unicam.ids2026.core.hackathon.Hackathon;
import it.unicam.ids2026.core.roles.User;
import it.unicam.ids2026.core.roles.staff.Mentore;
import it.unicam.ids2026.core.roles.team.Team;
import it.unicam.ids2026.core.violation.Violazione;
import it.unicam.ids2026.persistence.HackathonRepository;
import it.unicam.ids2026.persistence.UserRepository;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;
import java.util.Set;
import java.util.UUID;

/**
 * Gestisce le violazioni degli hackathon.
 */
@Service
@RequiredArgsConstructor
public class ViolationManager {
    private final HackathonRepository hackathonRepository;
    private final UserRepository userRepository;
    private final TeamManager teamManager;

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
        hackathonRepository.save(hackathon);
        return violazione;
    }

    public Violazione segnalaTeam(@NonNull UUID hackathonId,
                                  @NonNull UUID mentoreId,
                                  @NonNull UUID teamId,
                                  @NonNull String descrizione) {
        Hackathon hackathon = hackathonRepository.findById(hackathonId)
                .orElseThrow(() -> new NoSuchElementException("Nessun hackathon trovato con ID: " + hackathonId));
        User user = userRepository.findById(mentoreId)
                .orElseThrow(() -> new NoSuchElementException("Nessun utente trovato con ID: " + mentoreId));
        if (!(user instanceof Mentore mentore)) {
            throw new IllegalArgumentException("L'utente indicato non e un mentore");
        }
        Team team = teamManager.getTeam(teamId);
        return segnalaTeam(hackathon, team, mentore, descrizione);
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
