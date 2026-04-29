package it.unicam.ids2026.api.controller;

import it.unicam.ids2026.api.dto.request.AssegnaVincitoreRequest;
import it.unicam.ids2026.api.dto.response.MessageResponse;
import it.unicam.ids2026.core.hackathon.Hackathon;
import it.unicam.ids2026.core.managers.HackathonManager;
import it.unicam.ids2026.core.managers.TeamManager;
import it.unicam.ids2026.core.managers.WinningManager;
import it.unicam.ids2026.core.roles.team.Iscrizione;
import it.unicam.ids2026.core.roles.team.Team;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

/**
 * Controller REST per la gestione del vincitore dell'hackathon.
 */
@RestController
@RequestMapping("/api/hackathons/{hackathonId}")
public class WinningController {

    private final WinningManager winningManager;
    private final HackathonManager hackathonManager;
    private final TeamManager teamManager;

    public WinningController(WinningManager winningManager,
                             HackathonManager hackathonManager,
                             TeamManager teamManager) {
        this.winningManager = winningManager;
        this.hackathonManager = hackathonManager;
        this.teamManager = teamManager;
    }

    /**
     * GET /api/hackathons/{hackathonId}/winner-candidates
     * Corrisponde a: ottieniTeamConPunteggioMassimo(h) nel diagramma.
     * Ritorna la lista dei team a punteggio massimo oppure errore se ci sono
     * sottomissioni non valutate.
     *
     * @param hackathonId identificatore dell'hackathon
     * @return lista dei team candidati al vincitore
     */
    @GetMapping("/winner-candidates")
    public ResponseEntity<List<WinnerCandidateResponse>> getWinnerCandidates(
            @PathVariable UUID hackathonId) {
        Hackathon h = hackathonManager.getHackathon(hackathonId);
        List<Iscrizione> candidates = winningManager.ottieniTeamConPunteggioMassimo(h);
        List<WinnerCandidateResponse> response = candidates.stream()
                .map(iscrizione -> {
                    // Find the team from the iscritti map
                    Team team = h.getIscritti().entrySet().stream()
                            .filter(e -> e.getValue().equals(iscrizione))
                            .map(java.util.Map.Entry::getKey)
                            .findFirst()
                            .orElseThrow();
                    return WinnerCandidateResponse.from(team, iscrizione);
                })
                .toList();
        return ResponseEntity.ok(response);
    }

    /**
     * POST /api/hackathons/{hackathonId}/winner
     * Corrisponde a: assegnaVincitore(h, t) nel diagramma.
     * Setta il vincitore sull'hackathon.
     *
     * @param hackathonId identificatore dell'hackathon
     * @param request request contenente l'ID del team vincitore
     * @return messaggio di conferma
     */
    @PostMapping("/winner")
    public ResponseEntity<MessageResponse> assegnaVincitore(
            @PathVariable UUID hackathonId,
            @RequestBody AssegnaVincitoreRequest request) {
        Hackathon h = hackathonManager.getHackathon(hackathonId);
        Team t = teamManager.getTeam(request.teamId());
        winningManager.assegnaVincitore(h, t);
        return ResponseEntity.ok(new MessageResponse("Vincitore assegnato con successo"));
    }

    /**
     * DTO per la risposta contenente le informazioni del team candidato al vincitore.
     */
    public record WinnerCandidateResponse(
            String teamId,
            String teamName,
            double punteggio
    ) {
        /**
         * Crea una risposta a partire da un'iscrizione.
         *
         * @param team il team iscritto
         * @param iscrizione l'iscrizione del team
         * @return la risposta con le informazioni del candidato
         */
        public static WinnerCandidateResponse from(Team team, Iscrizione iscrizione) {
            return new WinnerCandidateResponse(
                    team.getId().toString(),
                    team.getNome(),
                    iscrizione.getSottomissione().getValutazione().voto()
            );
        }
    }
}
