package it.unicam.ids2026.core.managers;

import it.unicam.ids2026.core.hackathon.Hackathon;
import it.unicam.ids2026.core.hackathon.data.DatiHackathon;
import it.unicam.ids2026.core.hackathon.data.Intervallo;
import it.unicam.ids2026.core.hackathon.status.RappresentazioneStato;
import it.unicam.ids2026.core.roles.staff.Giudice;
import it.unicam.ids2026.core.roles.staff.Mentore;
import it.unicam.ids2026.core.roles.staff.Organizzatore;
import it.unicam.ids2026.core.roles.team.Iscrizione;
import it.unicam.ids2026.core.roles.team.Team;
import it.unicam.ids2026.core.roles.team.Utente;
import it.unicam.ids2026.persistence.HackathonRepository;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * Gestisce le operazioni relative agli hackathon.
 */
@Service
public class HackathonManager {

    private final HackathonRepository hackathonRepository;

    @Autowired
    public HackathonManager(HackathonRepository hackathonRepository) {
        this.hackathonRepository = hackathonRepository;
    }

    /**
     * Recupera un hackathon tramite il suo ID.
     *
     * @param id identificatore univoco dell'hackathon
     * @return l'hackathon cercato
     * @throws NoSuchElementException se l'hackathon non esiste
     */
    public Hackathon getHackathon(@NonNull UUID id) {
        return hackathonRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException(
                        "Nessun hackathon trovato con ID: " + id));
    }

    /**
     * Restituisce tutti gli hackathon presenti nel sistema.
     *
     * @return insieme di tutti gli hackathon
     */
    public Set<Hackathon> getHackathons() {
        return hackathonRepository.findAll();
    }

    /**
     * Crea un nuovo hackathon con i parametri forniti.
     * Valida che le date di iscrizione precedano la durata dell'hackathon.
     *
     * @param organizzatore organizzatore dell'hackathon
     * @param dati dati generali dell'hackathon (nome, luogo, premio, etc.)
     * @param giudice giudice assegnato all'hackathon
     * @param iscrizioni intervallo di tempo per le iscrizioni
     * @param durata intervallo di tempo dell'hackathon
     * @return l'hackathon creato
     * @throws IllegalArgumentException se le date non sono valide
     */
    public Hackathon creaHackathon(@NonNull Organizzatore organizzatore,
                                   @NonNull DatiHackathon dati,
                                   @NonNull Giudice giudice,
                                   @NonNull Intervallo iscrizioni,
                                   @NonNull Intervallo durata) {
        // Le iscrizioni devono chiudersi prima che inizi la durata
        if (iscrizioni.dataFine().isAfter(durata.dataInizio())
                || iscrizioni.dataInizio().isAfter(durata.dataInizio())
                || iscrizioni.dataFine().isBefore(iscrizioni.dataInizio())
                || durata.dataFine().isBefore(durata.dataInizio())) {
            throw new IllegalArgumentException("Range date inizio o durata invalide");
        }

        Hackathon newHackathon = new Hackathon(organizzatore, dati, giudice, iscrizioni, durata);
        if (hackathonRepository.existsById(newHackathon.getId())) {
            throw new IllegalArgumentException("Hackathon gia presente nel sistema");
        }
        return hackathonRepository.save(newHackathon);
    }

    /**
     * Avanza lo stato dell'hackathon al successivo stato del ciclo di vita.
     *
     * @param hackathon hackathon di cui avanzare lo stato
     */
    public void avanzaStato(@NonNull Hackathon hackathon) {
        hackathon.nextState();
        hackathonRepository.save(hackathon);
    }

    /**
     * Restituisce gli hackathon a cui è possibile iscriversi (stato ISCRIZIONE).
     *
     * @return insieme degli hackathon joinable
     */
    public Set<Hackathon> getJoinableHackathons() {
        return getFilteredHackathons(
                h -> h.getRappresentazioneStato() == RappresentazioneStato.ISCRIZIONE
        );
    }

    /**
     * Restituisce gli hackathon creati da un organizzatore specifico.
     *
     * @param organizzatore organizzatore di cui cercare gli hackathon
     * @return insieme degli hackathon creati dall'organizzatore
     */
    public Set<Hackathon> getHackathonCreati(Organizzatore organizzatore) {
        return getFilteredHackathons(
                h -> h.getOrganizzatore().equals(organizzatore)
        );
    }

    private Set<Hackathon> getFilteredHackathons(Predicate<Hackathon> predicate) {
        return hackathonRepository.findAll().stream()
                .filter(predicate)
                .collect(Collectors.toSet());
    }

    /**
     * Chiude le sottomissioni per un hackathon.
     * L'hackathon deve essere nello stato IN_CORSO.
     *
     * @param id identificatore dell'hackathon
     * @throws IllegalStateException se l'hackathon non è in corso
     */
    public void chiudiSottomissioni(UUID id) {
        Hackathon hackathon = getHackathon(id);
        if (hackathon.getRappresentazioneStato() != RappresentazioneStato.IN_CORSO) {
            throw new IllegalStateException(
                    "Impossibile chiudere le sottomissioni: l'hackathon non e in corso. Stato attuale: "
                            + hackathon.getRappresentazioneStato());
        }
        avanzaStato(hackathon);
    }

    /**
     * Aggiunge un singolo mentore all'hackathon.
     *
     * @param hackathon hackathon a cui aggiungere il mentore
     * @param mentore mentore da aggiungere
     * @throws IllegalArgumentException se il mentore è già presente
     */
    private void aggiungiMentore(@NonNull Hackathon hackathon, @NonNull Mentore mentore) {
        if (hackathon.getMentori().contains(mentore)) {
            throw new IllegalArgumentException("Mentore gia presente");
        }
        hackathon.aggiungiMentore(mentore);
        hackathonRepository.save(hackathon);
    }

    /**
     * Aggiunge una collezione di mentori all'hackathon.
     *
     * @param hackathon hackathon a cui aggiungere i mentori
     * @param mentori mentori da aggiungere
     */
    public void aggiungiMentori(@NonNull Hackathon hackathon, @NonNull Collection<Mentore> mentori) {
        mentori.forEach(m -> this.aggiungiMentore(hackathon, m));
    }

    /**
     * Iscrive il team di un utente all'hackathon.
     * L'utente deve appartenere a un team e il team non deve essere già iscritto.
     *
     * @param hackathon hackathon a cui iscrivere il team
     * @param utente utente che richiede l'iscrizione (deve avere un team)
     * @throws IllegalArgumentException se l'utente non ha team, è già iscritto, o il team è troppo grande
     */
    public void iscriviTeam(@NonNull Hackathon hackathon, @NonNull Utente utente) {
        if (!utente.haTeam()) {
            throw new IllegalArgumentException("Per iscriversi ad un Hackathon l'Utente deve avere un team");
        }
        Team team = utente.getTeam();
        if (hackathon.getIscritti().containsKey(team)) {
            throw new IllegalArgumentException("Team gia iscritto");
        }
        if (hackathon.getDatiHackathon().dimensioneMaxTeam() < team.getMembri().size()) {
            throw new IllegalArgumentException("Team troppo grande");
        }
        hackathon.iscriviTeam(team);
        hackathonRepository.save(hackathon);
    }
    
}
