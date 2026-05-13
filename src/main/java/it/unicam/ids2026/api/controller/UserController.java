package it.unicam.ids2026.api.controller;

import it.unicam.ids2026.api.dto.request.CreateUserRequest;
import it.unicam.ids2026.api.dto.response.UserResponse;
import it.unicam.ids2026.core.managers.UserManager;
import it.unicam.ids2026.core.roles.User;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Controller REST per la gestione degli utenti.
 */
@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserManager userManager;

    /**
     * Crea un nuovo utente a partire dai dati forniti nel body della richiesta.
     *
     * <p>I dati vengono validati tramite {@link Valid} e passati al
     * {@code userManager}, che si occupa della creazione dell'utente
     * in base al ruolo e alle informazioni anagrafiche. L'utente creato
     * viene quindi convertito in {@link UserResponse} e restituito con
     * codice di stato HTTP 201 (Created).</p>
     *
     * @param request i dati necessari per creare l'utente; non deve essere {@code null}
     * @return una risposta HTTP 201 contenente l'utente appena creato
     */
    @PostMapping
    public ResponseEntity<UserResponse> createUser(@Valid @RequestBody CreateUserRequest request) {
        User user = userManager.createUser(request.role().getRoleClass(), request.nome(), Optional.ofNullable(request.cognome()));
        return ResponseEntity.status(HttpStatus.CREATED).body(UserResponse.from(user));
    }


    /**
     * Restituisce l'insieme degli utenti attualmente non assegnati
     * ad alcun team o attività, secondo la logica definita nel
     * {@code userManager}.
     *
     * <p>Gli utenti vengono convertiti in {@link UserResponse} prima
     * di essere restituiti al client.</p>
     *
     * @return una risposta HTTP 200 contenente l'insieme degli utenti liberi
     */
    @GetMapping("/free")
    public ResponseEntity<Set<UserResponse>> getFreeUsers() {
        Set<UserResponse> users = userManager.getFreeUsers().stream()
                .map(UserResponse::from)
                .collect(Collectors.toSet());
        return ResponseEntity.ok(users);
    }


    /**
     * Restituisce l'utente identificato dal valore {@code id}.
     *
     * <p>L'utente viene recuperato tramite il {@code userManager}
     * e convertito in {@link UserResponse}. Se l'utente non esiste,
     * il comportamento dipende dalla logica interna di
     * {@code userManager.getUserById} (ad esempio eccezione o 404).</p>
     *
     * @param id l'identificatore dell'utente da recuperare
     * @return una risposta HTTP 200 contenente l'utente richiesto
     */
    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> getUser(@PathVariable UUID id) {
        return ResponseEntity.ok(UserResponse.from(userManager.getUserById(id)));
    }

}
