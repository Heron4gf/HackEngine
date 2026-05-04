package it.unicam.ids2026.api.controller;

import it.unicam.ids2026.api.dto.request.CreateSubmissionRequest;
import it.unicam.ids2026.api.dto.request.UpdateSubmissionRequest;
import it.unicam.ids2026.api.dto.response.SubmissionResponse;
import it.unicam.ids2026.core.hackathon.Hackathon;
import it.unicam.ids2026.core.hackathon.data.Sottomissione;
import it.unicam.ids2026.core.managers.HackathonManager;
import it.unicam.ids2026.core.managers.SubmissionManager;
import it.unicam.ids2026.core.managers.TeamManager;
import it.unicam.ids2026.core.roles.team.Team;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.util.UUID;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/hackathons/{hackathonId}/teams/{teamId}/submission")
public class SubmissionController {

    private final SubmissionManager submissionManager;
    private final HackathonManager hackathonManager;
    private final TeamManager teamManager;

    /**
     * Restituisce la sottomissione associata al team identificato da {@code nomeTeam}
     * all'interno dell'hackathon specificato da {@code hackathonId}.
     *
     * <p>La sottomissione viene recuperata tramite il {@code submissionManager}
     * e convertita in {@link SubmissionResponse} prima di essere restituita
     * al client. Se il team o l'hackathon non esistono, il comportamento dipende
     * dalla logica interna di {@code submissionManager.getSottomissione}.</p>
     *
     * @param hackathonId l'identificatore dell'hackathon di riferimento
     * @param nomeTeam il nome del team di cui recuperare la sottomissione
     * @return una risposta HTTP 200 contenente la sottomissione mappata
     */
    @GetMapping
    public ResponseEntity<SubmissionResponse> getSottomissione(
            @PathVariable UUID hackathonId,
            @PathVariable String nomeTeam) {
        Hackathon hackathon = hackathonManager.getHackathon(hackathonId);
        Team team = teamManager.getTeam(nomeTeam);
        Sottomissione sottomissione = submissionManager.ottieniSottomissione(hackathon, team);
        return ResponseEntity.ok(SubmissionResponse.from(sottomissione));
    }


    /**
     * Aggiorna o invia la sottomissione del team identificato da {@code nomeTeam}
     * per l'hackathon specificato da {@code hackathonId}.
     *
     * <p>I dati aggiornati della sottomissione vengono forniti tramite
     * {@link UpdateSubmissionRequest} e validati tramite {@link Valid}.
     * Il file allegato viene convertito in un'istanza di {@link File} e
     * passato al {@code submissionManager}, che si occupa dell'aggiornamento
     * della sottomissione. La risposta viene restituita come
     * {@link SubmissionResponse} con codice di stato HTTP 200.</p>
     *
     * @param hackathonId l'identificatore dell'hackathon di riferimento
     * @param nomeTeam il nome del team che invia o aggiorna la sottomissione
     * @param request i dati aggiornati della sottomissione, inclusa la descrizione e l'allegato
     * @return una risposta HTTP 200 contenente la sottomissione aggiornata
     */
    @PutMapping
    public ResponseEntity<SubmissionResponse> aggiornaSottomissione(
            @PathVariable UUID hackathonId,
            @PathVariable String nomeTeam,
            @Valid @RequestBody UpdateSubmissionRequest request) {
        Hackathon hackathon = hackathonManager.getHackathon(hackathonId);
        Team team = teamManager.getTeam(nomeTeam);
        Sottomissione sottomissione = submissionManager.aggiornaSottomissione(
                hackathon,
                team,
                request.descrizione(),
                new File(request.allegato())
        );
        return ResponseEntity.ok(SubmissionResponse.from(sottomissione));
    }


    /**
     * Invia la sottomissione del team identificato da {@code nomeTeam}
     * per l'hackathon specificato da {@code hackathonId}.
     *
     * <p>I dati della nuova sottomissione vengono forniti tramite
     * {@link UpdateSubmissionRequest} e validati tramite {@link Valid}.
     * Il file allegato viene convertito in un'istanza di {@link File} e
     * passato al {@code submissionManager}, che si occupa dell'aggiornamento
     * della sottomissione. La risposta viene restituita come
     * {@link SubmissionResponse} con codice di stato HTTP 200.</p>
     *
     * @param hackathonId l'identificatore dell'hackathon di riferimento
     * @param nomeTeam il nome del team che invia o aggiorna la sottomissione
     * @param request i dati aggiornati della sottomissione, inclusa la descrizione e l'allegato
     * @return una risposta HTTP 200 contenente la sottomissione aggiornata
     */
    @PutMapping
    public ResponseEntity<SubmissionResponse> inviaSottomissione(
            @PathVariable UUID hackathonId,
            @PathVariable String nomeTeam,
            @Valid @RequestBody CreateSubmissionRequest request) {
        Hackathon hackathon = hackathonManager.getHackathon(hackathonId);
        Team team = teamManager.getTeam(nomeTeam);
        Sottomissione sottomissione = submissionManager.inviaSottomissione(
                hackathon,
                team,
                request.name(),
                request.descrizione(),
                new File(request.allegato())
        );
        return ResponseEntity.ok(SubmissionResponse.from(sottomissione));
    }


}
