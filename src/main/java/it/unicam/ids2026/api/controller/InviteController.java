package it.unicam.ids2026.api.controller;

import it.unicam.ids2026.api.dto.request.InviteUserRequest;
import it.unicam.ids2026.api.dto.response.MessageResponse;
import it.unicam.ids2026.core.managers.InviteManager;
import it.unicam.ids2026.core.managers.TeamManager;
import it.unicam.ids2026.core.managers.UserManager;
import it.unicam.ids2026.core.roles.team.Invito;
import it.unicam.ids2026.core.roles.team.Team;
import it.unicam.ids2026.core.roles.team.Utente;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Set;
import java.util.UUID;

@RestController
@RequestMapping("/api/invites")
public class InviteController {

    private final InviteManager inviteManager;
    private final TeamManager teamManager;
    private final UserManager userManager;

    public InviteController(InviteManager inviteManager, TeamManager teamManager, UserManager userManager) {
        this.inviteManager = inviteManager;
        this.teamManager = teamManager;
        this.userManager = userManager;
    }

    @PostMapping
    public ResponseEntity<MessageResponse> inviteUser(@Valid @RequestBody InviteUserRequest request) {
        Team mittente = teamManager.getTeam(request.teamMittenteNome());
        Utente destinatario = (Utente) userManager.getUserById(request.destinatarioId());
        inviteManager.invitaUtente(mittente, destinatario);
        return ResponseEntity.ok(new MessageResponse("Invito inviato con successo"));
    }

    @GetMapping("/casella/{utenteId}")
    public ResponseEntity<Set<Invito>> getCasellaInviti(@PathVariable UUID utenteId) {
        Utente utente = (Utente) userManager.getUserById(utenteId);
        return ResponseEntity.ok(inviteManager.getCasellaInviti(utente));
    }

    @PostMapping("/accetta")
    public ResponseEntity<MessageResponse> accettaInvito(@RequestParam String nomeTeam, @RequestParam UUID utenteId) {
        Team mittente = teamManager.getTeam(nomeTeam);
        Utente destinatario = (Utente) userManager.getUserById(utenteId);

        Invito invito = inviteManager.findInvito(mittente, destinatario);
        inviteManager.accettaInvito(invito);
        return ResponseEntity.ok(new MessageResponse("Invito accettato"));
    }

    @PostMapping("/rifiuta")
    public ResponseEntity<MessageResponse> rifiutaInvito(@RequestParam String nomeTeam, @RequestParam UUID utenteId) {
        Team mittente = teamManager.getTeam(nomeTeam);
        Utente destinatario = (Utente) userManager.getUserById(utenteId);

        Invito invito = inviteManager.findInvito(mittente, destinatario);
        inviteManager.rifiutaInvito(invito);
        return ResponseEntity.ok(new MessageResponse("Invito rifiutato"));
    }
}
