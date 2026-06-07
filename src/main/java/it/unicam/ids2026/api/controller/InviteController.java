package it.unicam.ids2026.api.controller;

import it.unicam.ids2026.api.dto.request.InviteUserRequest;
import it.unicam.ids2026.api.dto.response.InvitoResponse;
import it.unicam.ids2026.api.dto.response.MessageResponse;
import it.unicam.ids2026.core.managers.InviteManager;
import it.unicam.ids2026.core.managers.TeamManager;
import it.unicam.ids2026.core.managers.UserManager;
import it.unicam.ids2026.core.roles.team.Invito;
import it.unicam.ids2026.core.roles.team.Team;
import it.unicam.ids2026.core.roles.team.Utente;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.NoSuchElementException;
import it.unicam.ids2026.api.dto.response.InvitoResponse;

import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Controller REST per la gestione degli inviti.
 */
@RestController
@RequestMapping("/api/invites")
@RequiredArgsConstructor
public class InviteController {

    private final InviteManager inviteManager;
    private final TeamManager teamManager;
    private final UserManager userManager;

    /**
     * Invia un invito a un utente.
     *
     * @param request dati dell'invito
     * @return messaggio di conferma
     */
    @PostMapping
    public ResponseEntity<MessageResponse> inviteUser(@Valid @RequestBody InviteUserRequest request) {
        Team mittente = teamManager.getTeam(request.teamMittenteNome());
        Utente destinatario = (Utente) userManager.getUserById(request.destinatarioId());
        inviteManager.invitaUtente(mittente, destinatario);
        return ResponseEntity.ok(new MessageResponse("Invito inviato con successo"));
    }

    /**
     * Restituisce la casella degli inviti di un utente.
     *
     * @param utenteId identificatore dell'utente
     * @return insieme di inviti ricevuti
     */
    @GetMapping("/casella/{utenteId}")
    public ResponseEntity<Set<InvitoResponse>> getCasellaInviti(@PathVariable UUID utenteId) {
        Utente utente = (Utente) userManager.getUserById(utenteId);
        Set<InvitoResponse> inviti = inviteManager.getCasellaInviti(utente).stream()
            .map(InvitoResponse::from)
            .collect(Collectors.toSet());
        return ResponseEntity.ok(inviti);
    }

    /**
     * Accetta un invito.
     *
     * @param nomeTeam nome del team mittente
     * @param utenteId identificatore dell'utente
     * @return messaggio di conferma
     */
    @PostMapping("/accetta")
    public ResponseEntity<MessageResponse> accettaInvito(@RequestParam String nomeTeam, @RequestParam UUID utenteId) {
        Team mittente = teamManager.getTeam(nomeTeam);
        Utente destinatario = (Utente) userManager.getUserById(utenteId);

        Invito invito = inviteManager.findInvito(mittente, destinatario);
        if (invito == null) {
            throw new NoSuchElementException("Invito non trovato");
        }
        inviteManager.accettaInvito(invito);
        return ResponseEntity.ok(new MessageResponse("Invito accettato"));
    }

    /**
     * Rifiuta un invito.
     *
     * @param nomeTeam nome del team mittente
     * @param utenteId identificatore dell'utente
     * @return messaggio di conferma
     */
    @PostMapping("/rifiuta")
    public ResponseEntity<MessageResponse> rifiutaInvito(@RequestParam String nomeTeam, @RequestParam UUID utenteId) {
        Team mittente = teamManager.getTeam(nomeTeam);
        Utente destinatario = (Utente) userManager.getUserById(utenteId);

        Invito invito = inviteManager.findInvito(mittente, destinatario);
        if (invito == null) {
            throw new NoSuchElementException("Invito non trovato");
        }
        inviteManager.rifiutaInvito(invito);
        return ResponseEntity.ok(new MessageResponse("Invito rifiutato"));
    }
}
