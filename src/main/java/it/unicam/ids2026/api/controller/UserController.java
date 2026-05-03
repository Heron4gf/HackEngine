package it.unicam.ids2026.api.controller;

import it.unicam.ids2026.api.dto.request.CreateUserRequest;
import it.unicam.ids2026.api.dto.response.UserResponse;
import it.unicam.ids2026.core.managers.UserManager;
import it.unicam.ids2026.core.roles.User;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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

    @PostMapping
    public ResponseEntity<UserResponse> createUser(@Valid @RequestBody CreateUserRequest request) {
        User user = userManager.createUser(request.role().name(), request.nome(), request.cognome());
        return ResponseEntity.status(HttpStatus.CREATED).body(UserResponse.from(user));
    }

    @GetMapping
    public ResponseEntity<Set<UserResponse>> getUsers(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String role) {
        Set<User> selected = selectUsers(name, role);
        Set<UserResponse> users = selected.stream()
                .map(UserResponse::from)
                .collect(Collectors.toSet());
        return ResponseEntity.ok(users);
    }

    @GetMapping("/free")
    public ResponseEntity<Set<UserResponse>> getFreeUsers() {
        Set<UserResponse> users = userManager.getFreeUsers().stream()
                .map(UserResponse::from)
                .collect(Collectors.toSet());
        return ResponseEntity.ok(users);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> getUser(@PathVariable UUID id) {
        return ResponseEntity.ok(UserResponse.from(userManager.getUserById(id)));
    }

    private Set<User> selectUsers(String name, String role) {
        if (name != null && role != null) {
            return userManager.getUsers(name, role);
        }
        if (name != null) {
            return userManager.getUserByName(name);
        }
        if (role != null) {
            return userManager.getUsersByRole(role);
        }
        return userManager.getUsers();
    }
}
