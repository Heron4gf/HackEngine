package it.unicam.ids2026.api.controller;

import it.unicam.ids2026.api.dto.request.CreateSupportRequest;
import it.unicam.ids2026.api.dto.response.MessageResponse;
import it.unicam.ids2026.api.dto.response.SupportRequestResponse;
import it.unicam.ids2026.core.hackathon.Hackathon;
import it.unicam.ids2026.core.hackathon.data.Disponibilita;
import it.unicam.ids2026.core.managers.HackathonManager;
import it.unicam.ids2026.core.managers.SupportRequestManager;
import it.unicam.ids2026.core.managers.TeamManager;
import it.unicam.ids2026.core.managers.UserManager;
import it.unicam.ids2026.core.roles.staff.Mentore;
import it.unicam.ids2026.core.roles.team.Team;
import it.unicam.ids2026.core.roles.team.Utente;
import it.unicam.ids2026.core.supportRequest.RichiestaSupporto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Controller REST per la gestione delle richieste di supporto e delle disponibilità
 * di utenti e mentori all'interno di un hackathon.
 */
@RestController
@RequestMapping("/api/hackathons/{hackathonId}/support-requests")
@RequiredArgsConstructor
public class SupportRequestController {

    private final SupportRequestManager supportRequestManager;
    private final HackathonManager hackathonManager;
    private final TeamManager teamManager;
    private final UserManager userManager;

    /**
     * Recupera tutte le richieste di supporto associate a un determinato hackathon.
     *
     * @param hackathonId l'identificativo univoco dell'hackathon
     * @return un {@link ResponseEntity} contenente il set di {@link SupportRequestResponse}
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
     * Crea una nuova richiesta di supporto per un team all'interno di un hackathon.
     *
     * @param hackathonId l'identificativo univoco dell'hackathon
     * @param request i dati della richiesta di supporto da creare
     * @return un {@link ResponseEntity} con stato 201 Created contenente la {@link SupportRequestResponse} creata
     */
    @PostMapping
    public ResponseEntity<SupportRequestResponse> creaRichiestaSupporto(
            @PathVariable UUID hackathonId,
            @Valid @RequestBody CreateSupportRequest request) {
        Hackathon hackathon = hackathonManager.getHackathon(hackathonId);
        Team team = teamManager.getTeam(request.nomeTeam(), hackathon);
        RichiestaSupporto richiesta = supportRequestManager.creaRichiestaSupporto(
                hackathon,
                team,
                request.titolo(),
                request.descrizione()
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(SupportRequestResponse.from(richiesta));
    }

    /**
     * Ottiene il calendario delle disponibilità di uno specifico utente per un determinato hackathon.
     * (Il calendario è un servizio esterno, non è possibile modificarlo)
     *
     * @param hackathonId l'identificativo univoco dell'hackathon
     * @param utenteId l'identificativo univoco dell'utente
     * @return un {@link ResponseEntity} contenente la {@link Disponibilita} dell'utente
     */
    @GetMapping("/calendario")
    public ResponseEntity<Disponibilita> ottieniCalendario(@PathVariable UUID hackathonId,
                                                           @RequestParam UUID utenteId) {
        Hackathon hackathon = hackathonManager.getHackathon(hackathonId);
        Utente utente = (Utente) userManager.getUserById(utenteId);
        Disponibilita disponibilita = supportRequestManager.ottieniCalendario(utente, hackathon);
        return ResponseEntity.ok(disponibilita);
    }

    /**
     * Ottiene la disponibilità oraria per un team all'interno di un hackathon.
     * (Le disponibilità sono l'unione del calendario dell'utente e le disponibilità inserite manualmente)
     *
     * @param hackathonId l'identificativo univoco dell'hackathon
     * @param teamName il nome del team
     * @return un {@link ResponseEntity} contenente la {@link Disponibilita} associata
     */
    @GetMapping("/disponibilita")
    public ResponseEntity<Disponibilita> ottieniDisponibilita(@PathVariable UUID hackathonId,
                                                              @RequestParam String teamName) {
        Hackathon hackathon = hackathonManager.getHackathon(hackathonId);
        Team team = teamManager.getTeam(teamName);
        Disponibilita disponibilita = supportRequestManager.ottieniDisponibilita(hackathon, team);
        return ResponseEntity.ok(disponibilita);
    }

    /**
     * Registra o aggiorna la disponibilità di un utente per un determinato hackathon.
     *
     * @param hackathonId l'identificativo univoco dell'hackathon
     * @param utenteId l'identificativo univoco dell'utente
     * @param nuovaDisponibilita l'oggetto contenente i nuovi slot di disponibilità
     * @return un {@link ResponseEntity} contenente un {@link MessageResponse} di conferma
     */
    @PutMapping("/disponibilita")
    public ResponseEntity<MessageResponse> registraDisponibilita(@PathVariable UUID hackathonId,
                                                                 @RequestParam UUID utenteId,
                                                                 @RequestParam Disponibilita nuovaDisponibilita) {
        Hackathon hackathon = hackathonManager.getHackathon(hackathonId);
        Utente utente = (Utente) userManager.getUserById(utenteId);
        supportRequestManager.registraDisponibilita(hackathon, utente, nuovaDisponibilita);
        return ResponseEntity.ok(new MessageResponse("Disponibilità registrata con successo"));
    }

    /**
     * Invia una risposta testuale da parte di un mentore a una richiesta di supporto di un team.
     *
     * @param hackathonId l'identificativo univoco dell'hackathon
     * @param teamName il nome del team che ha richiesto supporto
     * @param mentoreId l'identificativo univoco del mentore che risponde
     * @param message il testo della risposta
     * @return un {@link ResponseEntity} contenente un {@link MessageResponse} di conferma
     */
    @PostMapping("/risposta")
    public ResponseEntity<MessageResponse> rispondiTestualmente(@PathVariable UUID hackathonId,
                                                                @RequestParam String teamName,
                                                                @RequestParam UUID mentoreId,
                                                                @RequestParam String message) {
        Hackathon hackathon = hackathonManager.getHackathon(hackathonId);
        Team team = teamManager.getTeam(teamName);
        Mentore mentore = (Mentore) userManager.getUserById(mentoreId);
        RichiestaSupporto richiestaSupporto = supportRequestManager.ottieniRichiesta(hackathon, team);
        supportRequestManager.rispondiTestualmente(richiestaSupporto, mentore, message);
        return ResponseEntity.ok(new MessageResponse("Risposta registrata"));
    }

    /**
     * Fissa un incontro (call) tra un mentore e un team in una data e ora specifiche.
     *
     * @param hackathonId l'identificativo univoco dell'hackathon
     * @param teamName il nome del team
     * @param mentoreId l'identificativo univoco del mentore
     * @param dateTime la data e l'ora pianificate per la call
     * @return un {@link ResponseEntity} contenente un {@link MessageResponse} di conferma
     */
    @PostMapping("/call")
    public ResponseEntity<MessageResponse> fissaCall(@PathVariable UUID hackathonId,
                                                     @RequestParam String teamName,
                                                     @RequestParam UUID mentoreId,
                                                     @RequestParam LocalDateTime dateTime) {
        Hackathon hackathon = hackathonManager.getHackathon(hackathonId);
        Team team = teamManager.getTeam(teamName);
        Mentore mentore = (Mentore) userManager.getUserById(mentoreId);
        RichiestaSupporto richiestaSupporto = supportRequestManager.ottieniRichiesta(hackathon, team);
        supportRequestManager.fissaCall(richiestaSupporto, mentore, dateTime);
        return ResponseEntity.ok(new MessageResponse("Call fissata"));
    }
}