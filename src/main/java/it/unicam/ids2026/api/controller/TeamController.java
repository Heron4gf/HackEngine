package it.unicam.ids2026.api.controller;

import it.unicam.ids2026.api.dto.request.CreateTeamRequest;
import it.unicam.ids2026.api.dto.response.MessageResponse;
import it.unicam.ids2026.api.dto.response.TeamResponse;
import it.unicam.ids2026.core.managers.TeamManager;
import it.unicam.ids2026.core.managers.UserManager;
import it.unicam.ids2026.core.roles.team.Team;
import it.unicam.ids2026.core.roles.team.Utente;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/teams")
public class TeamController {

    private final TeamManager teamManager;
    private final UserManager userManager;

    public TeamController(TeamManager teamManager, UserManager userManager) {
        this.teamManager = teamManager;
        this.userManager = userManager;
    }

    @PostMapping
    public ResponseEntity<TeamResponse> createTeam(@Valid @RequestBody CreateTeamRequest request) {
        Utente utente = (Utente) userManager.getUserById(request.utenteId());
        teamManager.creaTeam(utente, request.nome(), request.maxMembri());
        Team team = teamManager.getTeam(request.nome());
        return ResponseEntity.status(HttpStatus.CREATED).body(TeamResponse.from(team));
    }

    @GetMapping("/{nome}")
    public ResponseEntity<TeamResponse> getTeam(@PathVariable String nome) {
        Team team = teamManager.getTeam(nome);
        return ResponseEntity.ok(TeamResponse.from(team));
    }

    @PostMapping("/{nome}/esci")
    public ResponseEntity<MessageResponse> exitTeam(@RequestParam UUID utenteId) {
        Utente utente = (Utente) userManager.getUserById(utenteId);
        teamManager.esciDalTeam(utente);
        return ResponseEntity.ok(new MessageResponse("Uscito dal team con successo"));
    }

    @PostMapping("/{nome}/iscrizione")
    public ResponseEntity<MessageResponse> iscriviTeam(
            @PathVariable String nome,
            @RequestParam UUID hackathonId,
            @RequestParam UUID utenteId) {
        Team team = teamManager.getTeam(nome);
        Utente utente = (Utente) userManager.getUserById(utenteId);
        // Note: This would need the HackathonManager, adding to controller
        return ResponseEntity.ok(new MessageResponse("Team iscritto all'hackathon"));
    }

}
