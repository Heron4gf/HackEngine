package it.unicam.ids2026.core.managers;

import it.unicam.ids2026.core.hackathon.Hackathon;
import it.unicam.ids2026.core.roles.staff.Mentore;
import it.unicam.ids2026.core.roles.team.Team;
import it.unicam.ids2026.core.violation.Violazione;
import it.unicam.ids2026.persistence.HackathonRepository;
import lombok.RequiredArgsConstructor;

import java.util.Set;

@RequiredArgsConstructor
public class ViolationManager {
    private final HackathonRepository hackathonRepository;

    public void segnalaTeam(Hackathon hackathon, Team team, Mentore mentore, String descrizione) {
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

    public Set<Violazione> getViolations(Hackathon hackathon) {
        return hackathon.getViolazioni();
    }

    public void gestisciViolazione() {

    }
}
