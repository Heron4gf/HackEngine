package it.unicam.ids2026.api.controller;

import it.unicam.ids2026.api.dto.request.CreateTeamRequest;
import it.unicam.ids2026.api.dto.response.MessageResponse;
import it.unicam.ids2026.api.dto.response.TeamResponse;
import it.unicam.ids2026.core.hackathon.Hackathon;
import it.unicam.ids2026.core.managers.HackathonManager;
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

/**
 * Controller REST per la gestione dei team.
 */
@RestController
@RequestMapping("/api/teams")
public class TeamController {

    private final TeamManager teamManager;
    private final UserManager userManager;
    private final HackathonManager hackathonManager;

    public TeamController(TeamManager teamManager, UserManager userManager, HackathonManager hackathonManager) {
        this.teamManager = teamManager;
        this.userManager = userManager;
        this.hackathonManager = hackathonManager;
    }

    /**
     * Crea un nuovo team.
     *
     * @param request dati per la creazione del team
     * @return il team creato con stato 201
     */
    @PostMapping
    public ResponseEntity<TeamResponse> createTeam(@Valid @RequestBody CreateTeamRequest request) {
        Utente utente = (Utente) userManager.getUserById(request.utenteId());
        teamManager.creaTeam(utente, request.nome(), request.maxMembri());
        Team team = teamManager.getTeam(request.nome());
        return ResponseEntity.status(HttpStatus.CREATED).body(TeamResponse.from(team));
    }

    /**
     * Restituisce un team specifico.
     *
     * @param nome nome del team
     * @return il team cercato
     */
    @GetMapping("/{nome}")
    public ResponseEntity<TeamResponse> getTeam(@PathVariable String nome) {
        Team team = teamManager.getTeam(nome);
        return ResponseEntity.ok(TeamResponse.from(team));
    }

    /**
     * Permette a un utente di uscire da un team.
     *
     * @param utenteId identificatore dell'utente
     * @return messaggio di conferma
     */
    @PostMapping("/{nome}/esci")
    public ResponseEntity<MessageResponse> exitTeam(@RequestParam UUID utenteId) {
        Utente utente = (Utente) userManager.getUserById(utenteId);
        teamManager.esciDalTeam(utente);
        return ResponseEntity.ok(new MessageResponse("Uscito dal team con successo"));
    }

    /**
     * Iscrive un team a un hackathon.
     *
     * @param nome nome del team
     * @param hackathonId identificatore dell'hackathon
     * @param utenteId identificatore dell'utente che richiede l'iscrizione
     * @return messaggio di conferma
     */
    @PostMapping("/{nome}/iscrizione")
    public ResponseEntity<MessageResponse> iscriviTeam(
            @PathVariable String nome,
            @RequestParam UUID hackathonId,
            @RequestParam UUID utenteId) {
        Team team = teamManager.getTeam(nome);
        Utente utente = (Utente) userManager.getUserById(utenteId);
        // Verifica che l'utente appartenga al team specificato
        if (!team.equals(utente.getTeam())) {
            throw new IllegalArgumentException("L'utente indicato non appartiene al team " + nome);
        }
        Hackathon hackathon = hackathonManager.getHackathon(hackathonId);
        hackathonManager.iscriviTeam(hackathon, utente);
        return ResponseEntity.ok(new MessageResponse("Team iscritto all'hackathon"));
    }

}
