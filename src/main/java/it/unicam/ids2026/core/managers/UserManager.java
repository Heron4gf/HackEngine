package it.unicam.ids2026.core.managers;

import it.unicam.ids2026.core.roles.User;
import it.unicam.ids2026.persistence.UserRepository;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Gestisce le operazioni relative agli utenti.
 */
@Service
public class UserManager {

    private final UserRepository userRepository;

    @Autowired
    public UserManager(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Cerca utenti per nome.
     *
     * @param nome nome da cercare
     * @return insieme di utenti con il nome specificato
     * @throws NoSuchElementException se non vengono trovati utenti
     */
    public Set<User> getUsers(@NonNull String nome) {
        Set<User> found = userRepository.findAll().stream()
                .filter(user -> nome.equals(user.getNome()))
                .collect(Collectors.toSet());
        if (found.isEmpty()) {
            throw new NoSuchElementException("Nessun utente trovato con nome: " + nome);
        }
        return found;
    }

    /**
     * Recupera un utente tramite il suo ID.
     *
     * @param id identificatore univoco dell'utente
     * @return l'utente cercato
     * @throws NoSuchElementException se l'utente non esiste
     */
    public User getUserById(@NonNull UUID id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Nessun utente trovato con ID: " + id));
    }

    /**
     * Restituisce tutti gli utenti con un determinato ruolo.
     *
     * @param roleClass classe del ruolo da cercare
     * @param <T> tipo del ruolo
     * @return insieme di utenti con il ruolo specificato
     */
    public <T extends User> Set<T> getUsersByRole(Class<T> roleClass) {
        return userRepository.findAll().stream()
                .filter(roleClass::isInstance)
                .map(roleClass::cast)
                .collect(Collectors.toSet());
    }

    /**
     * Restituisce tutti gli utenti presenti nel sistema.
     *
     * @return insieme di tutti gli utenti
     */
    public Set<User> getUsers() {
        return userRepository.findAll();
    }

    /**
     * Aggiunge un nuovo utente al sistema.
     *
     * @param user utente da aggiungere
     * @throws IllegalArgumentException se l'utente è già presente
     */
    public void addUser(@NonNull User user) {
        if (userRepository.existsById(user.getId())) {
            throw new IllegalArgumentException("Utente gia presente nel sistema");
        }
        userRepository.save(user);
    }
}
