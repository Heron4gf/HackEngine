package it.unicam.ids2026.api.controller;

import it.unicam.ids2026.api.dto.request.ElaboraPagamentoRequest;
import it.unicam.ids2026.api.dto.response.ClassificaResponse;
import it.unicam.ids2026.api.dto.response.TeamResponse;
import it.unicam.ids2026.api.dto.response.TransazioneResponse;
import it.unicam.ids2026.core.hackathon.Hackathon;
import it.unicam.ids2026.core.managers.HackathonManager;
import it.unicam.ids2026.core.managers.TeamManager;
import it.unicam.ids2026.core.managers.WinningManager;
import it.unicam.ids2026.core.roles.team.Team;
import it.unicam.ids2026.core.transaction.Transaction;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Controller REST per la gestione della classifica e dei vincitori degli hackathon.
 */
@RestController
@RequestMapping("/api/hackathons/{hackathonId}")
public class RankingController {

    private final HackathonManager hackathonManager;
    private final TeamManager teamManager;
    private final WinningManager winningManager;

    public RankingController(HackathonManager hackathonManager, TeamManager teamManager, WinningManager winningManager) {
        this.hackathonManager = hackathonManager;
        this.teamManager = teamManager;
        this.winningManager = winningManager;
    }

    /**
     * Restituisce la classifica completa dell'hackathon.
     *
     * @param hackathonId ID dell'hackathon
     * @return classifica ordinata per punteggio
     */
    @GetMapping("/classifica")
    public ResponseEntity<ClassificaResponse> ottieniClassifica(@PathVariable UUID hackathonId) {
        Hackathon hackathon = hackathonManager.getHackathon(hackathonId);
        var classifica = winningManager.ottieniClassifica(hackathon);

        return ResponseEntity.ok(ClassificaResponse.from(
                hackathon.getId(),
                hackathon.getDatiHackathon().nome(),
                classifica
        ));
    }

    /**
     * Determina i vincitori dell'hackathon (in caso di parità restituisce tutti i parimerito).
     *
     * @param hackathonId ID dell'hackathon
     * @return lista dei team vincitori
     */
    @GetMapping("/vincitori")
    public ResponseEntity<List<TeamResponse>> determinaVincitori(@PathVariable UUID hackathonId) {
        Hackathon hackathon = hackathonManager.getHackathon(hackathonId);
        List<Team> vincitori = winningManager.determinaVincitori(hackathon);

        List<TeamResponse> response = vincitori.stream()
                .map(TeamResponse::from)
                .collect(Collectors.toList());

        return ResponseEntity.ok(response);
    }

    /**
     * Restituisce il voto di un team specifico.
     *
     * @param hackathonId ID dell'hackathon
     * @param teamId      ID del team
     * @return il voto del team
     */
    @GetMapping("/team/{teamId}/voto")
    public ResponseEntity<VotoTeamResponse> ottieniVotoTeam(
            @PathVariable UUID hackathonId,
            @PathVariable UUID teamId) {
        Hackathon hackathon = hackathonManager.getHackathon(hackathonId);
        Team team = teamManager.getTeamById(teamId);
        
        Integer voto = winningManager.ottieniVotoTeam(hackathon, team);
        
        return ResponseEntity.ok(new VotoTeamResponse(teamId, voto));
    }

    /**
     * Elabora il pagamento del premio al team vincitore.
     *
     * @param hackathonId ID dell'hackathon
     * @param request     request contenente l'ID del team vincitore
     * @return risultato del pagamento
     */
    @PostMapping("/elabora-pagamento")
    public ResponseEntity<TransazioneResponse> elaboraPagamentoPremio(
            @PathVariable UUID hackathonId,
            @RequestBody @Valid ElaboraPagamentoRequest request) {
        Hackathon hackathon = hackathonManager.getHackathon(hackathonId);
        Team vincitore = teamManager.getTeamById(request.teamVincitoreId());
        
        Transaction transazione = winningManager.elaboraPagamentoPremio(hackathon, vincitore);

        return ResponseEntity.ok(TransazioneResponse.from(transazione));
    }

    /**
     * Response DTO per il voto di un team.
     */
    public record VotoTeamResponse(UUID teamId, Integer voto) {}
}
