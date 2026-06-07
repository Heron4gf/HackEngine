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
import lombok.RequiredArgsConstructor;
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
@RequiredArgsConstructor
public class TeamController {

    private final TeamManager teamManager;
    private final UserManager userManager;
    private final HackathonManager hackathonManager;

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
     * Restituisce l'insieme dei team disponibili.
     *
     * <p>Se il parametro {@code hackathonId} non è fornito, vengono restituiti
     * tutti i team presenti nel sistema tramite il {@code teamManager}.
     * Se invece {@code hackathonId} è specificato, vengono restituiti solo i team
     * associati all'hackathon corrispondente, recuperato tramite
     * {@code hackathonManager}.</p>
     *
     * <p>I team vengono convertiti in {@link TeamResponse} prima di essere
     * restituiti al client.</p>
     *
     * @param hackathonId l'identificatore dell'hackathon da cui filtrare i team;
     *                    può essere {@code null} per ottenere tutti i team
     * @return una risposta HTTP 200 contenente l'insieme dei team mappati
     */
    @GetMapping
    public ResponseEntity<Set<TeamResponse>> getTeams(@RequestParam(required = false) UUID hackathonId) {
        Set<Team> source = hackathonId == null
                ? teamManager.getTeams()
                : hackathonManager.getHackathon(hackathonId).getTeams();
        Set<TeamResponse> teams = source.stream()
                .map(TeamResponse::from)
                .collect(Collectors.toSet());
        return ResponseEntity.ok(teams);
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
    @PostMapping("/esci")
    public ResponseEntity<MessageResponse> esciDalTeam(@RequestParam UUID utenteId) {
        Utente utente = (Utente) userManager.getUserById(utenteId);
        teamManager.esciDalTeam(utente);
        return ResponseEntity.ok(new MessageResponse("Uscito dal team con successo"));
    }

    /**
     * Iscrive un team a un hackathon.
     *
     * @param nomeTeam nome del team
     * @param hackathonId identificatore dell'hackathon
     * @return messaggio di conferma
     */
    @PostMapping("/{nomeTeam}/iscrizione")
    public ResponseEntity<MessageResponse> iscriviTeam(
            @PathVariable String nomeTeam,
            @RequestParam UUID hackathonId) {
        Team team = teamManager.getTeam(nomeTeam);
        Hackathon hackathon = hackathonManager.getHackathon(hackathonId);
        hackathonManager.iscriviTeam(hackathon, team);
        return ResponseEntity.ok(new MessageResponse("Team iscritto all'hackathon"));
    }

}
