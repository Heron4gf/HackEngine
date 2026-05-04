package it.unicam.ids2026.api.controller;

import it.unicam.ids2026.api.dto.request.CreateSupportRequest;
import it.unicam.ids2026.api.dto.response.SupportRequestResponse;
import it.unicam.ids2026.core.hackathon.Hackathon;
import it.unicam.ids2026.core.managers.HackathonManager;
import it.unicam.ids2026.core.managers.SupportRequestManager;
import it.unicam.ids2026.core.supportRequest.RichiestaSupporto;
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
@RequestMapping("/api/hackathons/{hackathonId}/support-requests")
@RequiredArgsConstructor
public class SupportRequestController {

    private final SupportRequestManager supportRequestManager;
    private final HackathonManager hackathonManager;

    /**
     * Restituisce l'insieme delle richieste di supporto associate
     * all'hackathon identificato da {@code hackathonId}.
     *
     * <p>L'hackathon viene recuperato tramite {@code hackathonManager},
     * quindi le richieste vengono ottenute tramite il
     * {@code supportRequestManager} e convertite in
     * {@link SupportRequestResponse} prima di essere restituite.</p>
     *
     * @param hackathonId l'identificatore dell'hackathon di cui recuperare le richieste
     * @return una risposta HTTP 200 contenente l'insieme delle richieste di supporto mappate
     */
    @GetMapping
    public ResponseEntity<Set<SupportRequestResponse>> getRichieste(@PathVariable UUID hackathonId) {
        Hackathon hackathon = hackathonManager.getHackathon(hackathonId);
        Set<SupportRequestResponse> richieste = supportRequestManager.visualizzaRichieste(hackathon).stream()
                .map(SupportRequestResponse::from)
                .collect(Collectors.toSet());
        return ResponseEntity.ok(richieste);
    }


    /**
     * Crea una nuova richiesta di supporto per un team partecipante
     * all'hackathon identificato da {@code hackathonId}.
     *
     * <p>I dati necessari alla creazione della richiesta vengono forniti
     * tramite {@link CreateSupportRequest} e validati tramite {@link Valid}.
     * La richiesta viene creata dal {@code supportRequestManager} e
     * restituita come {@link SupportRequestResponse} con codice di stato
     * HTTP 201 (Created).</p>
     *
     * @param hackathonId l'identificatore dell'hackathon a cui appartiene il team
     * @param request i dati necessari per creare la richiesta di supporto
     * @return una risposta HTTP 201 contenente la richiesta appena creata
     */
    @PostMapping
    public ResponseEntity<SupportRequestResponse> creaRichiestaSupport(
            @PathVariable UUID hackathonId,
            @Valid @RequestBody CreateSupportRequest request) {
        RichiestaSupporto richiesta = supportRequestManager.creaRichiestaSupporto(
                request.titolo(),
                request.descrizione(),
                hackathonId,
                request.nomeTeam()
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(SupportRequestResponse.from(richiesta));
    }

}
