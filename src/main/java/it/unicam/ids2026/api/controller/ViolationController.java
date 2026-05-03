package it.unicam.ids2026.api.controller;

import it.unicam.ids2026.api.dto.request.CreateViolationRequest;
import it.unicam.ids2026.api.dto.response.ViolationResponse;
import it.unicam.ids2026.core.hackathon.Hackathon;
import it.unicam.ids2026.core.managers.HackathonManager;
import it.unicam.ids2026.core.managers.ViolationManager;
import it.unicam.ids2026.core.violation.Violazione;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/hackathons/{hackathonId}/violations")
public class ViolationController {

    private final ViolationManager violationManager;
    private final HackathonManager hackathonManager;

    public ViolationController(ViolationManager violationManager, HackathonManager hackathonManager) {
        this.violationManager = violationManager;
        this.hackathonManager = hackathonManager;
    }

    @GetMapping
    public ResponseEntity<Set<ViolationResponse>> getViolations(@PathVariable UUID hackathonId) {
        Hackathon hackathon = hackathonManager.getHackathon(hackathonId);
        Set<ViolationResponse> violations = violationManager.getViolations(hackathon).stream()
                .map(ViolationResponse::from)
                .collect(Collectors.toSet());
        return ResponseEntity.ok(violations);
    }

    @PostMapping
    public ResponseEntity<ViolationResponse> segnalaTeam(
            @PathVariable UUID hackathonId,
            @Valid @RequestBody CreateViolationRequest request) {
        Violazione violazione = violationManager.segnalaTeam(
                hackathonId,
                request.mentoreId(),
                request.teamId(),
                request.descrizione()
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(ViolationResponse.from(violazione));
    }
}
