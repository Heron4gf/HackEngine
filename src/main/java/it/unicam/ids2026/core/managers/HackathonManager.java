package it.unicam.ids2026.core.managers;

import it.unicam.ids2026.core.hackathon.Hackathon;
import it.unicam.ids2026.core.hackathon.data.DatiHackathon;
import it.unicam.ids2026.core.hackathon.data.Intervallo;
import it.unicam.ids2026.core.hackathon.status.RappresentazioneStato;
import it.unicam.ids2026.core.roles.staff.Giudice;
import it.unicam.ids2026.core.roles.staff.Mentore;
import it.unicam.ids2026.core.roles.staff.Organizzatore;
import it.unicam.ids2026.core.roles.team.Team;
import it.unicam.ids2026.core.roles.team.Utente;
import it.unicam.ids2026.persistence.HackathonRepository;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.UUID;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Service
public class HackathonManager {

    private final HackathonRepository hackathonRepository;

    @Autowired
    public HackathonManager(HackathonRepository hackathonRepository) {
        this.hackathonRepository = hackathonRepository;
    }

    public Hackathon getHackathon(@NonNull UUID id) {
        return hackathonRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException(
                        "Nessun hackathon trovato con ID: " + id));
    }

    public Set<Hackathon> getHackathons() {
        return hackathonRepository.findAll();
    }

    public Hackathon creaHackathon(@NonNull Organizzatore organizzatore,
                                   @NonNull DatiHackathon dati,
                                   @NonNull Giudice giudice,
                                   @NonNull Intervallo iscrizioni,
                                   @NonNull Intervallo durata) {
        if (iscrizioni.dataFine().isAfter(durata.dataInizio())
                || iscrizioni.dataInizio().isAfter(durata.dataInizio())
                || iscrizioni.dataFine().isBefore(iscrizioni.dataInizio())
                || durata.dataFine().isBefore(durata.dataInizio())
                || !validaDati(dati)) {
            throw new IllegalArgumentException("Range date inizio o durata invalide");
        }

        Hackathon newHackathon = new Hackathon(organizzatore, dati, giudice, iscrizioni, durata);
        if (hackathonRepository.existsById(newHackathon.getId())) {
            throw new IllegalArgumentException("Hackathon gia presente nel sistema");
        }
        return hackathonRepository.save(newHackathon);
    }

    private boolean validaDati(DatiHackathon datiHackathon) {
        // TODO: usare Jakarta Validator
        return true;
    }

    public void avanzaStato(@NonNull Hackathon hackathon) {
        hackathon.nextState();
        hackathonRepository.save(hackathon);
    }

    public Set<Hackathon> getJoinableHackathons() {
        return getFilteredHackathons(
                h -> h.getRappresentazioneStato() == RappresentazioneStato.ISCRIZIONE
        );
    }

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

    public void chiudiSottomissioni(UUID id) {
        Hackathon hackathon = getHackathon(id);
        if (hackathon.getRappresentazioneStato() != RappresentazioneStato.IN_CORSO) {
            throw new IllegalStateException(
                    "Impossibile chiudere le sottomissioni: l'hackathon non e in corso. Stato attuale: "
                            + hackathon.getRappresentazioneStato());
        }
        avanzaStato(hackathon);
    }

    private void aggiungiMentore(@NonNull Hackathon hackathon, @NonNull Mentore mentore) {
        if (hackathon.getMentori().contains(mentore)) {
            throw new IllegalArgumentException("Mentore gia presente");
        }
        hackathon.aggiungiMentore(mentore);
        hackathonRepository.save(hackathon);
    }

    public void aggiungiMentori(@NonNull Hackathon hackathon, @NonNull Collection<Mentore> mentori) {
        mentori.forEach(m -> this.aggiungiMentore(hackathon, m));
    }

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
