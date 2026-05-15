package it.unicam.ids2026.api.controller;

import it.unicam.ids2026.api.dto.request.CreateHackathonRequest;
import it.unicam.ids2026.api.dto.response.HackathonResponse;
import it.unicam.ids2026.api.dto.response.MessageResponse;
import it.unicam.ids2026.api.dto.response.TeamResponse;
import it.unicam.ids2026.core.hackathon.Hackathon;
import it.unicam.ids2026.core.hackathon.data.DatiHackathon;
import it.unicam.ids2026.core.hackathon.data.Intervallo;
import it.unicam.ids2026.core.managers.HackathonManager;
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

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/hackathons")
public class HackathonController {

    private final HackathonManager hackathonManager;
    private final UserManager userManager;

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

    @GetMapping
    public ResponseEntity<Set<HackathonResponse>> getAllHackathons() {
        Set<HackathonResponse> hackathons = hackathonManager.getHackathons()
                .stream()
                .map(HackathonResponse::from)
                .collect(Collectors.toSet());
        return ResponseEntity.ok(hackathons);
    }

    @GetMapping("/{id}")
    public ResponseEntity<HackathonResponse> getHackathon(@PathVariable UUID id) {
        Hackathon hackathon = hackathonManager.getHackathon(id);
        return ResponseEntity.ok(HackathonResponse.from(hackathon));
    }

    @GetMapping("/{id}/teams")
    public ResponseEntity<Set<TeamResponse>> getTeams(@PathVariable UUID id) {
        Hackathon hackathon = hackathonManager.getHackathon(id);
        Set<TeamResponse> teams = hackathon.getTeams().stream()
                .map(TeamResponse::from)
                .collect(Collectors.toSet());
        return ResponseEntity.ok(teams);
    }

    @GetMapping("/joinable")
    public ResponseEntity<Set<HackathonResponse>> getJoinableHackathons() {
        Set<HackathonResponse> hackathons = hackathonManager.getJoinableHackathons().stream()
                .map(HackathonResponse::from)
                .collect(Collectors.toSet());
        return ResponseEntity.ok(hackathons);
    }

    @GetMapping("/by-organizer/{organizzatoreId}")
    public ResponseEntity<Set<HackathonResponse>> getHackathonsByOrganizer(@PathVariable UUID organizzatoreId) {
        Organizzatore organizzatore = (Organizzatore) userManager.getUserById(organizzatoreId);
        Set<HackathonResponse> hackathons = hackathonManager.getHackathonCreati(organizzatore).stream()
                .map(HackathonResponse::from)
                .collect(Collectors.toSet());
        return ResponseEntity.ok(hackathons);
    }

    @PostMapping("/{hackathonId}/aggiungi-mentore")
    public ResponseEntity<MessageResponse> aggiungiMentore(@PathVariable UUID hackathonId, Set<UUID> mentoriId) {
        Hackathon hackathon = hackathonManager.getHackathon(hackathonId);
        Set<Mentore> mentori = mentoriId.stream()
                .map(userManager::getUserById)
                .map(Mentore.class::cast)
                .collect(Collectors.toSet());
        hackathonManager.aggiungiMentori(hackathon, mentori);
        return ResponseEntity.ok(new MessageResponse("Mentore aggiunto all'hackathon"));
    }

    @PostMapping("/{id}/chiudi-sottomissioni")
    public ResponseEntity<MessageResponse> chiudiSottomissioni(@PathVariable UUID id) {
        Hackathon hackathon = hackathonManager.getHackathon(id);
        hackathonManager.chiudiSottomissioni(hackathon);
        return ResponseEntity.ok(new MessageResponse("Sottomissioni chiuse con successo"));
    }

    @PostMapping("/{id}/avanza-stato")
    public ResponseEntity<MessageResponse> avanzaStato(@PathVariable UUID id) {
        Hackathon hackathon = hackathonManager.getHackathon(id);
        hackathonManager.avanzaStato(hackathon);
        return ResponseEntity.ok(new MessageResponse("Stato avanzato con successo"));
    }
}
