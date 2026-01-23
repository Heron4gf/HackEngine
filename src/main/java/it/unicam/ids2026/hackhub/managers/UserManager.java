package it.unicam.ids2026.hackhub.managers;

import it.unicam.ids2026.hackhub.roles.User;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class UserManager {
    private final Set<User> users;

    /**
     * Recupera tutti gli utenti che corrispondono al nome specificato.
     * Restituisce un set vuoto se il nome passato è null.
     *
     * @param nome Il nome da cercare tra gli utenti.
     * @return Un set contenente gli utenti trovati o un set vuoto.
     */
    public Set<User> getUsers(@NonNull String nome) {
        return users.stream()
                .filter(user -> nome.equals(user.getNome()))
                .collect(Collectors.toSet());
    }

    /**
     * Cerca un utente specifico tramite il suo identificativo univoco (UUID).
     *
     * @param id L'UUID dell'utente da recuperare.
     * @return L'oggetto User corrispondente, oppure null se non trovato o se l'ID è null.
     */
    public User getUserById(@NonNull UUID id) {
        return users.stream()
                .filter(user -> id.equals(user.getId()))
                .findFirst()
                .orElse(null);
    }
}