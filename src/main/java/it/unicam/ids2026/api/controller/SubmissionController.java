package it.unicam.ids2026.api.controller;

import it.unicam.ids2026.api.dto.request.CreateSubmissionRequest;
import it.unicam.ids2026.api.dto.request.EvaluationRequest;
import it.unicam.ids2026.api.dto.request.UpdateSubmissionRequest;
import it.unicam.ids2026.api.dto.response.MessageResponse;
import it.unicam.ids2026.api.dto.response.SubmissionResponse;
import it.unicam.ids2026.core.hackathon.Hackathon;
import it.unicam.ids2026.core.hackathon.data.Sottomissione;
import it.unicam.ids2026.core.managers.HackathonManager;
import it.unicam.ids2026.core.managers.SubmissionManager;
import it.unicam.ids2026.core.managers.TeamManager;
import it.unicam.ids2026.core.roles.team.Team;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/hackathons/{hackathonId}")
public class SubmissionController {

    private final SubmissionManager submissionManager;
    private final HackathonManager hackathonManager;
    private final TeamManager teamManager;

    /**
     * Recupera la sottomissione di uno specifico team per un determinato hackathon.
     *
     * @param hackathonId l'identificativo univoco dell'hackathon
     * @param teamId l'identificativo del team
     * @return un {@link ResponseEntity} contenente la {@link SubmissionResponse} con i dettagli della sottomissione
     */
    @GetMapping("/teams/{teamId}/submission")
    public ResponseEntity<SubmissionResponse> getSottomissione(
            @PathVariable UUID hackathonId,
            @PathVariable String teamId) {
        Hackathon hackathon = hackathonManager.getHackathon(hackathonId);
        Team team = teamManager.getTeam(teamId);
        Sottomissione sottomissione = submissionManager.ottieniSottomissione(hackathon, team);
        return ResponseEntity.ok(SubmissionResponse.from(sottomissione));
    }

    /**
     * Invia una nuova sottomissione per un team all'interno di un hackathon.
     *
     * @param hackathonId l'identificativo univoco dell'hackathon
     * @param teamId l'identificativo del team
     * @param request i dati della sottomissione da creare
     * @return un {@link ResponseEntity} con stato 21 Created contenente la {@link SubmissionResponse} della sottomissione creata
     */
    @PostMapping("/teams/{teamId}/submission")
    public ResponseEntity<SubmissionResponse> inviaSottomissione(
            @PathVariable UUID hackathonId,
            @PathVariable String teamId,
            @Valid @RequestBody CreateSubmissionRequest request) {
        Hackathon hackathon = hackathonManager.getHackathon(hackathonId);
        Team team = teamManager.getTeam(teamId);
        Sottomissione sottomissione = submissionManager.inviaSottomissione(
                hackathon,
                team,
                request.name(),
                request.descrizione(),
                new File(request.allegato())
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(SubmissionResponse.from(sottomissione));
    }

    /**
     * Recupera tutte le sottomissioni associate a un determinato hackathon.
     *
     * @param hackathonId l'identificativo univoco dell'hackathon
     * @return un {@link ResponseEntity} contenente la lista delle {@link SubmissionResponse}
     */
    @GetMapping("/submission/all")
    public ResponseEntity<List<SubmissionResponse>> ottieniSottomissioni(
            @PathVariable UUID hackathonId
    ) {
        Hackathon hackathon = hackathonManager.getHackathon(hackathonId);
        return ResponseEntity.ok(
                submissionManager.ottieniSottomissioni(hackathon).values().stream()
                        .map(SubmissionResponse::from)
                        .toList()
        );
    }

    /**
     * Assegna una valutazione e un giudizio a una specifica sottomissione.
     *
     * @param hackathonId l'identificativo univoco dell'hackathon
     * @param teamId l'identificativo del team
     * @param request i dati della valutazione (voto e giudizio)
     * @return un {@link ResponseEntity} vuoto con stato 24 No Content
     */
    @PostMapping("/teams/{teamId}/submission/evaluate")
    public ResponseEntity<MessageResponse> assegnaValutazione(
            @PathVariable UUID hackathonId,
            @PathVariable String teamId,
            @Valid @RequestBody EvaluationRequest request) {
        Hackathon hackathon = hackathonManager.getHackathon(hackathonId);
        Team team = teamManager.getTeam(teamId);
        Sottomissione sottomissione = submissionManager.ottieniSottomissione(hackathon, team);

        submissionManager.assegnaValutazione(sottomissione, request.voto(), request.giudizio());
        return ResponseEntity.ok(new MessageResponse("Sottomissione valutata"));
    }

    /**
     * Aggiorna i dati di una sottomissione esistente per un team in un hackathon.
     *
     * @param hackathonId l'identificativo univoco dell'hackathon
     * @param teamId l'identificativo del team
     * @param request i nuovi dati per l'aggiornamento della sottomissione
     * @return un {@link ResponseEntity} contenente la {@link SubmissionResponse} aggiornata
     */
    @PutMapping("/teams/{teamId}/submission")
    public ResponseEntity<SubmissionResponse> aggiornaSottomissione(
            @PathVariable UUID hackathonId,
            @PathVariable String teamId,
            @Valid @RequestBody UpdateSubmissionRequest request) {
        Hackathon hackathon = hackathonManager.getHackathon(hackathonId);
        Team team = teamManager.getTeam(teamId);
        Sottomissione sottomissione = submissionManager.aggiornaSottomissione(
                hackathon,
                team,
                request.descrizione(),
                new File(request.allegato())
        );
        return ResponseEntity.ok(SubmissionResponse.from(sottomissione));
    }
}