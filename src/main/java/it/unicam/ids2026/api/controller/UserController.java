package it.unicam.ids2026.api.controller;

import it.unicam.ids2026.api.dto.request.CreateUserRequest;
import it.unicam.ids2026.api.dto.response.UserResponse;
import it.unicam.ids2026.core.managers.UserManager;
import it.unicam.ids2026.core.roles.User;
import it.unicam.ids2026.core.roles.staff.Giudice;
import it.unicam.ids2026.core.roles.staff.Mentore;
import it.unicam.ids2026.core.roles.staff.Organizzatore;
import it.unicam.ids2026.core.roles.team.Utente;
import jakarta.validation.Valid;
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

/**
 * Controller REST per la gestione degli utenti.
 */
@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserManager userManager;

    public UserController(UserManager userManager) {
        this.userManager = userManager;
    }

    /**
     * Crea un nuovo utente.
     *
     * @param request dati per la creazione dell'utente
     * @return l'utente creato con stato 201
     */
    @PostMapping
    public ResponseEntity<UserResponse> createUser(@Valid @RequestBody CreateUserRequest request) {
        User user = switch (request.role()) {
            case UTENTE -> new Utente(request.nome());
            case ORGANIZZATORE -> new Organizzatore(request.nome(), requireCognome(request));
            case GIUDICE -> new Giudice(request.nome(), requireCognome(request));
            case MENTORE -> new Mentore(request.nome(), requireCognome(request));
        };
        userManager.addUser(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(UserResponse.from(user));
    }

    /**
     * Restituisce tutti gli utenti.
     *
     * @return lista di tutti gli utenti
     */
    @GetMapping
    public ResponseEntity<Set<UserResponse>> getUsers() {
        Set<UserResponse> users = userManager.getUsers().stream()
                .map(UserResponse::from)
                .collect(Collectors.toSet());
        return ResponseEntity.ok(users);
    }

    /**
     * Restituisce un utente specifico.
     *
     * @param id identificatore dell'utente
     * @return l'utente cercato
     */
    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> getUser(@PathVariable UUID id) {
        return ResponseEntity.ok(UserResponse.from(userManager.getUserById(id)));
    }

    /**
     * Verifica e restituisce il cognome per i membri dello staff.
     * Il cognome è obbligatorio per Organizzatore, Giudice e Mentore.
     */
    private String requireCognome(CreateUserRequest request) {
        if (request.cognome() == null || request.cognome().isBlank()) {
            throw new IllegalArgumentException("Il cognome e obbligatorio per i membri dello staff");
        }
        return request.cognome();
    }
}
