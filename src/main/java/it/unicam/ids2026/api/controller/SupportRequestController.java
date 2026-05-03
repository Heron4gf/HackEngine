package it.unicam.ids2026.api.controller;

import it.unicam.ids2026.api.dto.request.CreateSupportRequest;
import it.unicam.ids2026.api.dto.response.SupportRequestResponse;
import it.unicam.ids2026.core.hackathon.Hackathon;
import it.unicam.ids2026.core.managers.HackathonManager;
import it.unicam.ids2026.core.managers.SupportRequestManager;
import it.unicam.ids2026.core.supportRequest.RichiestaSupporto;
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
@RequestMapping("/api/hackathons/{hackathonId}/support-requests")
public class SupportRequestController {

    private final SupportRequestManager supportRequestManager;
    private final HackathonManager hackathonManager;

    public SupportRequestController(SupportRequestManager supportRequestManager, HackathonManager hackathonManager) {
        this.supportRequestManager = supportRequestManager;
        this.hackathonManager = hackathonManager;
    }

    @GetMapping
    public ResponseEntity<Set<SupportRequestResponse>> getRichieste(@PathVariable UUID hackathonId) {
        Hackathon hackathon = hackathonManager.getHackathon(hackathonId);
        Set<SupportRequestResponse> richieste = supportRequestManager.visualizzaRichieste(hackathon).stream()
                .map(SupportRequestResponse::from)
                .collect(Collectors.toSet());
        return ResponseEntity.ok(richieste);
    }

    @PostMapping
    public ResponseEntity<SupportRequestResponse> creaRichiestaSupport(
            @PathVariable UUID hackathonId,
            @Valid @RequestBody CreateSupportRequest request) {
        RichiestaSupporto richiesta = supportRequestManager.creaRichiestaSupporto(
                request.titolo(),
                request.descrizione(),
                hackathonId,
                request.teamId()
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(SupportRequestResponse.from(richiesta));
    }
}
