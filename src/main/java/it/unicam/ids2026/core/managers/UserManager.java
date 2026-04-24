package it.unicam.ids2026.core.managers;

import it.unicam.ids2026.core.roles.User;
import lombok.Getter;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class UserManager {

    @Getter
    private final Set<User> users = new HashSet<>();

    @Autowired
    public UserManager() {
    }

    /**
     * Recupera tutti gli utenti che corrispondono al nome specificato.
     *
     * @param nome Il nome da cercare tra gli utenti.
     * @return Un set contenente gli utenti trovati.
     * @throws NoSuchElementException se non vengono trovati utenti con il nome specificato.
     */
    public Set<User> getUsers(@NonNull String nome) {
        Set<User> found = users.stream()
                .filter(user -> nome.equals(user.getNome()))
                .collect(Collectors.toSet());
        if (found.isEmpty()) {
            throw new NoSuchElementException("Nessun utente trovato con nome: " + nome);
        }
        return found;
    }

    /**
     * Cerca un utente specifico tramite il suo identificativo univoco (UUID).
     *
     * @param id L'UUID dell'utente da recuperare.
     * @return L'oggetto User corrispondente.
     * @throws NoSuchElementException se nessun utente con l'ID specificato esiste.
     */
    public User getUserById(@NonNull UUID id) {
        return users.stream()
                .filter(user -> id.equals(user.getId()))
                .findFirst()
                .orElseThrow(() -> new NoSuchElementException("Nessun utente trovato con ID: " + id));
    }

    public <T extends User> Set<T> getUsersByRole(Class<T> roleClass) {
        return users.stream()
                .filter(roleClass::isInstance)
                .map(roleClass::cast)
                .collect(Collectors.toSet());
    }

    /**
     * Aggiunge un utente al sistema.
     *
     * @param user L'utente da aggiungere.
     * @throws IllegalArgumentException se l'utente è già presente.
     */
    public void addUser(@NonNull User user) {
        if (users.contains(user)) {
            throw new IllegalArgumentException("Utente già presente nel sistema");
        }
        users.add(user);
    }
}
