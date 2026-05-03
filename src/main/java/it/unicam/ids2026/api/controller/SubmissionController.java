package it.unicam.ids2026.api.controller;

import it.unicam.ids2026.api.dto.request.UpdateSubmissionRequest;
import it.unicam.ids2026.api.dto.response.SubmissionResponse;
import it.unicam.ids2026.core.hackathon.data.Sottomissione;
import it.unicam.ids2026.core.managers.SubmissionManager;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.util.UUID;

@RestController
@RequestMapping("/api/hackathons/{hackathonId}/teams/{teamId}/submission")
public class SubmissionController {

    private final SubmissionManager submissionManager;

    public SubmissionController(SubmissionManager submissionManager) {
        this.submissionManager = submissionManager;
    }

    @GetMapping
    public ResponseEntity<SubmissionResponse> getSottomissione(
            @PathVariable UUID hackathonId,
            @PathVariable UUID teamId) {
        Sottomissione sottomissione = submissionManager.getSottomissione(hackathonId, teamId);
        return ResponseEntity.ok(SubmissionResponse.from(sottomissione));
    }

    @PutMapping
    public ResponseEntity<SubmissionResponse> inviaSottomissione(
            @PathVariable UUID hackathonId,
            @PathVariable UUID teamId,
            @Valid @RequestBody UpdateSubmissionRequest request) {
        Sottomissione sottomissione = submissionManager.aggiornaSottomissione(
                hackathonId,
                teamId,
                request.descrizione(),
                new File(request.allegato())
        );
        return ResponseEntity.ok(SubmissionResponse.from(sottomissione));
    }
}
