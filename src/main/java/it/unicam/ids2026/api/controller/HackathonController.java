package it.unicam.ids2026.api.controller;

import it.unicam.ids2026.api.dto.request.CreateHackathonRequest;
import it.unicam.ids2026.api.dto.response.HackathonResponse;
import it.unicam.ids2026.api.dto.response.MessageResponse;
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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Controller REST per la gestione degli hackathon.
 */
@RestController
@RequestMapping("/api/hackathons")
public class HackathonController {

    private final HackathonManager hackathonManager;
    private final StaffManager staffManager;
    private final UserManager userManager;

    public HackathonController(HackathonManager hackathonManager, StaffManager staffManager, UserManager userManager) {
        this.hackathonManager = hackathonManager;
        this.staffManager = staffManager;
        this.userManager = userManager;
    }

    /**
     * Crea un nuovo hackathon.
     *
     * @param request dati per la creazione dell'hackathon
     * @return l'hackathon creato con stato 201
     */
    @PostMapping
    public ResponseEntity<HackathonResponse> createHackathon(@Valid @RequestBody CreateHackathonRequest request) {
        Organizzatore organizzatore = (Organizzatore) userManager.getUserById(request.organizzatoreId());
        Giudice giudice = (Giudice) userManager.getUserById(request.giudiceId());

        DatiHackathon dati = new DatiHackathon(
                request.nome(),
                request.luogo(),
                new MoneyAmount(
                        request.premioInDenaro(),
                        request.currency()
                ),
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
     * Restituisce tutti gli hackathon.
     *
     * @return lista di tutti gli hackathon
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
     * Restituisce un hackathon specifico.
     *
     * @param id identificatore dell'hackathon
     * @return l'hackathon cercato
     */
    @GetMapping("/{id}")
    public ResponseEntity<HackathonResponse> getHackathon(@PathVariable UUID id) {
        Hackathon hackathon = hackathonManager.getHackathon(id);
        return ResponseEntity.ok(HackathonResponse.from(hackathon));
    }

    /**
     * Restituisce gli hackathon a cui è possibile iscriversi.
     *
     * @return lista degli hackathon in fase di iscrizione
     */
    @GetMapping("/joinable")
    public ResponseEntity<Set<HackathonResponse>> getJoinableHackathons() {
        Set<HackathonResponse> hackathons = hackathonManager.getJoinableHackathons().stream()
                .map(HackathonResponse::from)
                .collect(Collectors.toSet());
        return ResponseEntity.ok(hackathons);
    }

    /**
     * Resitutuisce gli hackathon creati da un organizzatore
     * @param organizzatore l'organizzatore degli hackathon
     * @return gli hackathon creati da tale organizzatore
     */

    @GetMapping("/by-organizer/{organizzatore}")
    public ResponseEntity<Set<HackathonResponse>> getHackathonsByOrganizer(@PathVariable Organizzatore organizzatore) {
        Set<HackathonResponse> hackathons = hackathonManager.getHackathonCreati(organizzatore).stream().map(
                HackathonResponse::from).collect(Collectors.toSet());
        return ResponseEntity.ok(hackathons);
    }

    /**
     * Aggiunge un mentore agli hackathon creati
     *
     */
    @PostMapping("/{hackathon}/aggiungi-mentore")
    public ResponseEntity<MessageResponse> aggiungiMentore(@PathVariable Hackathon hackathon,  Set<Mentore> mentori) {
        hackathonManager.aggiungiMentori(hackathon, mentori);
        return ResponseEntity.ok(new MessageResponse("Mentore aggiunto all'hackathon"));
    }

    /**
     * Chiude le sottomissioni per un hackathon.
     *
     * @param id identificatore dell'hackathon
     * @return messaggio di conferma
     */
    @PostMapping("/{id}/chiudi-sottomissioni")
    public ResponseEntity<MessageResponse> chiudiSottomissioni(@PathVariable UUID id) {
        hackathonManager.chiudiSottomissioni(id);
        return ResponseEntity.ok(new MessageResponse("Sottomissioni chiuse con successo"));
    }

    /**
     * Avanza lo stato di un hackathon.
     *
     * @param id identificatore dell'hackathon
     * @return messaggio di conferma
     */
    @PostMapping("/{id}/avanza-stato")
    public ResponseEntity<MessageResponse> avanzaStato(@PathVariable UUID id) {
        Hackathon hackathon = hackathonManager.getHackathon(id);
        hackathonManager.avanzaStato(hackathon);
        return ResponseEntity.ok(new MessageResponse("Stato avanzato con successo"));
    }

}
