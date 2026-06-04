package it.unicam.ids2026.api.controller;

import it.unicam.ids2026.api.dto.request.CreateHackathonRequest;
import it.unicam.ids2026.api.dto.response.HackathonResponse;
import it.unicam.ids2026.api.dto.response.MessageResponse;
import it.unicam.ids2026.api.dto.response.TeamResponse;
import it.unicam.ids2026.api.dto.response.UserResponse;
import it.unicam.ids2026.core.hackathon.Hackathon;
import it.unicam.ids2026.core.hackathon.data.DatiHackathon;
import it.unicam.ids2026.core.hackathon.data.Intervallo;
import it.unicam.ids2026.core.managers.HackathonManager;
import it.unicam.ids2026.core.managers.StaffManager;
import it.unicam.ids2026.core.managers.UserManager;
import it.unicam.ids2026.core.roles.staff.Giudice;
import it.unicam.ids2026.core.roles.staff.Mentore;
import it.unicam.ids2026.core.roles.staff.Organizzatore;
import it.unicam.ids2026.core.transaction.MoneyAmount;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Controller REST per la gestione del ciclo di vita degli hackathon, delle iscrizioni dei team e dello staff.
 */
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/hackathons")
public class HackathonController {

    private final HackathonManager hackathonManager;
    private final UserManager userManager;
    private final StaffManager staffManager;

    /**
     * Crea un nuovo hackathon con i dettagli specificati, l'organizzatore e il giudice indicato.
     *
     * @param request DTO contenente i dettagli per la creazione dell'hackathon
     * @return ResponseEntity contenente l'HackathonResponse creato e lo stato HTTP 201
     */
    @PostMapping
    public ResponseEntity<HackathonResponse> createHackathon(@Valid @RequestBody CreateHackathonRequest request) {
        Organizzatore organizzatore = (Organizzatore) userManager.getUserById(request.organizzatoreId());
        Giudice giudice = (Giudice) userManager.getUserById(request.giudiceId());

        DatiHackathon dati = new DatiHackathon(
                request.nome(),
                request.luogo(),
                new MoneyAmount(request.premioInDenaro(), request.currency()),
                request.dimensioneMaxTeam(),
                request.regolamento()
        );

        Intervallo iscrizioni = new Intervallo(
                request.iscrizioni().dataInizio(),
                request.iscrizioni().dataFine()
        );

        Intervallo durata = new Intervallo(
                request.durata().dataInizio(),
                request.durata().dataFine()
        );

        Hackathon hackathon = hackathonManager.creaHackathon(organizzatore, dati, giudice, iscrizioni, durata);
        return ResponseEntity.status(HttpStatus.CREATED).body(HackathonResponse.from(hackathon));
    }

    /**
     * Recupera tutti gli hackathon esistenti.
     *
     * @return ResponseEntity contenente l'insieme di tutti gli HackathonResponse
     */
    @GetMapping
    public ResponseEntity<Set<HackathonResponse>> getAllHackathons() {
        Set<HackathonResponse> hackathons = hackathonManager.getHackathons()
                .stream()
                .map(HackathonResponse::from)
                .collect(Collectors.toSet());
        return ResponseEntity.ok(hackathons);
    }

    /**
     * Recupera uno specifico hackathon tramite il suo identificativo unico.
     *
     * @param id UUID dell'hackathon da recuperare
     * @return ResponseEntity contenente l'HackathonResponse corrispondente
     */
    @GetMapping("/{id}")
    public ResponseEntity<HackathonResponse> getHackathon(@PathVariable UUID id) {
        Hackathon hackathon = hackathonManager.getHackathon(id);
        return ResponseEntity.ok(HackathonResponse.from(hackathon));
    }

    /**
     * Recupera tutti i team iscritti a uno specifico hackathon.
     *
     * @param id UUID dell'hackathon
     * @return ResponseEntity contenente l'insieme dei TeamResponse iscritti
     */
    @GetMapping("/{id}/teams")
    public ResponseEntity<Set<TeamResponse>> getTeams(@PathVariable UUID id) {
        Hackathon hackathon = hackathonManager.getHackathon(id);
        Set<TeamResponse> teams = hackathon.getTeams().stream()
                .map(TeamResponse::from)
                .collect(Collectors.toSet());
        return ResponseEntity.ok(teams);
    }

    /**
     * Recupera tutti gli hackathon a cui è attualmente possibile iscriversi.
     *
     * @return ResponseEntity contenente l'insieme degli HackathonResponse disponibili
     */
    @GetMapping("/joinable")
    public ResponseEntity<Set<HackathonResponse>> getJoinableHackathons() {
        Set<HackathonResponse> hackathons = hackathonManager.getJoinableHackathons().stream()
                .map(HackathonResponse::from)
                .collect(Collectors.toSet());
        return ResponseEntity.ok(hackathons);
    }

    /**
     * Recupera tutti gli hackathon creati da uno specifico organizzatore.
     *
     * @param organizzatoreId UUID dell'organizzatore
     * @return ResponseEntity contenente l'insieme degli HackathonResponse associati
     */
    @GetMapping("/by-organizer/{organizzatoreId}")
    public ResponseEntity<Set<HackathonResponse>> getHackathonDiOrganizzatore(@PathVariable UUID organizzatoreId) {
        Organizzatore organizzatore = (Organizzatore) userManager.getUserById(organizzatoreId);
        Set<HackathonResponse> hackathons = hackathonManager.getHackathonCreati(organizzatore).stream()
                .map(HackathonResponse::from)
                .collect(Collectors.toSet());
        return ResponseEntity.ok(hackathons);
    }

    /**
     * Recupera i mentori disponibili per essere assegnati a uno specifico hackathon.
     *
     * @param id UUID dell'hackathon
     * @return ResponseEntity contenente l'insieme dei UserResponse associati ai mentori disponibili
     */
    @GetMapping("/{id}/mentori-disponibili")
    public ResponseEntity<Set<UserResponse>> getMentoriDisponibili(@PathVariable UUID id) {
        Hackathon hackathon = hackathonManager.getHackathon(id);
        Set<UserResponse> mentori = staffManager.ottieniMentoriDisponibili(hackathon).stream()
                .map(UserResponse::from)
                .collect(Collectors.toSet());
        return ResponseEntity.ok(mentori);
    }

    /**
     * Associa un insieme di mentori a uno specifico hackathon.
     *
     * @param hackathonId UUID dell'hackathon di destinazione
     * @param mentoriId insieme degli UUID dei mentori da aggiungere nel corpo della richiesta
     * @return ResponseEntity contenente un MessageResponse di conferma dell'operazione
     */
    @PostMapping("/{hackathonId}/aggiungi-mentore")
    public ResponseEntity<MessageResponse> aggiungiMentori(@PathVariable UUID hackathonId, @RequestBody Set<UUID> mentoriId) {
        Hackathon hackathon = hackathonManager.getHackathon(hackathonId);
        Set<Mentore> mentori = mentoriId.stream()
                .map(userManager::getUserById)
                .map(Mentore.class::cast)
                .collect(Collectors.toSet());
        hackathonManager.aggiungiMentori(hackathon, mentori);
        return ResponseEntity.ok(new MessageResponse("Mentore aggiunto all'hackathon"));
    }

    /**
     * Fa avanzare lo stato interno del ciclo di vita dello specifico hackathon.
     *
     * @param id UUID dell'hackathon
     * @return ResponseEntity contenente un MessageResponse di conferma dell'operazione
     */
    @PostMapping("/{id}/avanza-stato")
    public ResponseEntity<MessageResponse> avanzaStato(@PathVariable UUID id) {
        Hackathon hackathon = hackathonManager.getHackathon(id);
        hackathonManager.avanzaStato(hackathon);
        return ResponseEntity.ok(new MessageResponse("Stato avanzato con successo"));
    }
}