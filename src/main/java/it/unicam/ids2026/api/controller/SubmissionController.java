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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.util.UUID;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/hackathons/{hackathonId}/teams/{teamId}/submission")
public class SubmissionController {

    private final SubmissionManager submissionManager;
    private final HackathonManager hackathonManager;
    private final TeamManager teamManager;

    @GetMapping
    public ResponseEntity<SubmissionResponse> getSottomissione(
            @PathVariable UUID hackathonId,
            @PathVariable String teamId) {
        Hackathon hackathon = hackathonManager.getHackathon(hackathonId);
        Team team = teamManager.getTeam(teamId);
        Sottomissione sottomissione = submissionManager.ottieniSottomissione(hackathon, team);
        return ResponseEntity.ok(SubmissionResponse.from(sottomissione));
    }

    @PostMapping
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

    @PutMapping
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
