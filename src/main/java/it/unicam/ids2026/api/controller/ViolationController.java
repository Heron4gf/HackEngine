package it.unicam.ids2026.api.controller;

import it.unicam.ids2026.api.dto.request.CreateViolationRequest;
import it.unicam.ids2026.api.dto.response.ViolationResponse;
import it.unicam.ids2026.core.hackathon.Hackathon;
import it.unicam.ids2026.core.managers.HackathonManager;
import it.unicam.ids2026.core.managers.ViolationManager;
import it.unicam.ids2026.core.violation.Violazione;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
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
@RequiredArgsConstructor
public class ViolationController {

    private final ViolationManager violationManager;
    private final HackathonManager hackathonManager;

    /**
     * Restituisce l'insieme delle violazioni registrate per l'hackathon
     * identificato da {@code hackathonId}.
     *
     * <p>Le violazioni vengono recuperate tramite il {@code violationManager}
     * e convertite in {@link ViolationResponse} prima di essere restituite
     * al client. Se l'hackathon non esiste, il comportamento dipende dalla
     * logica interna di {@code hackathonManager.getHackathon}.</p>
     *
     * @param hackathonId l'identificatore dell'hackathon di cui recuperare le violazioni
     * @return una risposta HTTP 200 contenente l'insieme delle violazioni mappate
     */
    @GetMapping
    public ResponseEntity<Set<ViolationResponse>> getViolations(@PathVariable UUID hackathonId) {
        Hackathon hackathon = hackathonManager.getHackathon(hackathonId);
        Set<ViolationResponse> violations = violationManager.getViolations(hackathon).stream()
                .map(ViolationResponse::from)
                .collect(Collectors.toSet());
        return ResponseEntity.ok(violations);
    }


    /**
     * Registra una nuova violazione per un team partecipante all'hackathon
     * identificato da {@code hackathonId}.
     *
     * <p>I dati della violazione vengono forniti tramite il body della richiesta
     * sotto forma di {@link CreateViolationRequest}. La violazione viene creata
     * tramite il {@code violationManager} e restituita al client come
     * {@link ViolationResponse} con codice di stato HTTP 201 (Created).</p>
     *
     * @param hackathonId l'identificatore dell'hackathon a cui appartiene il team segnalato
     * @param request i dati necessari per creare la violazione; validati tramite {@link Valid}
     * @return una risposta HTTP 201 contenente la violazione appena creata
     */
    @PostMapping
    public ResponseEntity<ViolationResponse> segnalaTeam(
            @PathVariable UUID hackathonId,
            @Valid @RequestBody CreateViolationRequest request) {
        Violazione violazione = violationManager.segnalaTeam(
                hackathonId,
                request.mentoreId(),
                request.nomeTeam(),
                request.descrizione()
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(ViolationResponse.from(violazione));
    }
}
