package it.unicam.ids2026.core.managers;

import it.unicam.ids2026.api.events.TeamJoinListener;
import it.unicam.ids2026.core.events.EventPublisher;
import it.unicam.ids2026.core.roles.User;
import it.unicam.ids2026.core.roles.staff.Giudice;
import it.unicam.ids2026.core.roles.staff.Mentore;
import it.unicam.ids2026.core.roles.staff.Organizzatore;
import it.unicam.ids2026.core.roles.team.Team;
import it.unicam.ids2026.core.roles.team.Utente;
import it.unicam.ids2026.persistence.UserRepository;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.function.BiFunction;
import java.util.stream.Collectors;

/**
 * Gestisce le operazioni relative agli utenti.
 */
@Service
public class UserManager implements TeamJoinListener {

    private final UserRepository userRepository;

    public UserManager(UserRepository userRepository, EventPublisher eventPublisher) {
        this.userRepository = userRepository;
        eventPublisher.registerListener(this);
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
     * Restituisce tutti gli utenti di tipo Utente che non appartengono a nessun team.
     *
     * @return insieme degli utenti disponibili senza team
     */
    public Set<Utente> getFreeUsers() {
        return getUsersByRole(Utente.class).stream()
                .filter(utente -> utente.getTeam() == null)
                .collect(Collectors.toSet());
    }


    private void addUser(@NonNull User user) {
        if (userRepository.existsById(user.getId())) {
            throw new IllegalArgumentException("Utente gia presente nel sistema");
        }
        userRepository.save(user);
    }

    private final Map<Class<? extends User>, BiFunction<String, Optional<String>, User>> factories =
            Map.of(
                    Utente.class, (nome, cognome) -> new Utente(nome),
                    Organizzatore.class, (nome, cognome) -> new Organizzatore(nome, cognome.orElseThrow()),
                    Giudice.class, (nome, cognome) -> new Giudice(nome, cognome.orElse(null)),
                    Mentore.class, (nome, cognome) -> new Mentore(nome, cognome.orElse(null))
            );

    /**
     * Crea un nuovo utente istanziando la sottoclasse di {@link User} indicata.
     * La classe passata determina quale tipo concreto di utente viene creato.
     *
     * <p>Alcuni ruoli richiedono obbligatoriamente il cognome (es. Organizzatore),
     * mentre altri lo ignorano o lo accettano come opzionale.</p>
     *
     * @param role    la classe concreta del tipo di utente da creare; non deve essere {@code null}
     * @param nome    il nome dell'utente; non deve essere {@code null}
     * @param cognome il cognome dell'utente, opzionale a seconda del ruolo; non deve essere {@code null}
     * @return l'utente creato
     * @throws IllegalArgumentException se la classe non corrisponde a un ruolo supportato
     */
    public User createUser(@NonNull Class<? extends User> role,
                           @NonNull String nome,
                           @NonNull Optional<String> cognome) {

        BiFunction<String, Optional<String>, User> factory = factories.get(role);

        if (factory == null) {
            throw new IllegalArgumentException("Ruolo non valido: " + role.getSimpleName());
        }

        User user = factory.apply(nome, cognome);
        addUser(user);
        return user;
    }


    @Override
    public void notifyEnterTeam(@NonNull Team team, @NonNull Utente utente) {
        utente.setTeam(team);
    }
}
