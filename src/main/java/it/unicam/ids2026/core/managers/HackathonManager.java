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
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.util.Collection;
import java.util.Set;
import java.util.UUID;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class HackathonManager {

    @Getter
    private final Set<Hackathon> hackathons;

    /**
     * Recupera un hackathon esistente tramite il suo identificativo univoco.
     *
     * @param id L'UUID dell'hackathon da cercare.
     * @return L'oggetto Hackathon se trovato, altrimenti null.
     */
    public Hackathon getHackathon(@NonNull UUID id) {
        return hackathons.stream()
                .filter(h -> h.getId().equals(id))
                .findFirst()
                .orElse(null);
    }

    /**
     * Factory method che crea un nuovo hackathon e lo registra nel sistema.
     *
     * @param organizzatore L'organizzatore responsabile dell'evento.
     * @param dati          I metadati descrittivi dell'hackathon.
     * @param giudice       Il giudice principale assegnato.
     * @param iscrizioni    L'intervallo temporale per le iscrizioni.
     * @param durata        L'intervallo temporale di svolgimento dell'evento.
     * @return L'istanza del nuovo Hackathon creato.
     */
    public Hackathon creaHackathon(@NonNull Organizzatore organizzatore,
                                   @NonNull DatiHackathon dati,
                                   @NonNull Giudice giudice,
                                   @NonNull Intervallo iscrizioni,
                                   @NonNull Intervallo durata) {

        // logica validazione date
        if(
                iscrizioni.dataFine().isAfter(durata.dataInizio())
                || iscrizioni.dataInizio().isAfter(durata.dataInizio())
                || iscrizioni.dataFine().isBefore(iscrizioni.dataInizio())
                || durata.dataFine().isBefore(durata.dataInizio())
                || !validaDati(dati)
        ) {
            throw new IllegalArgumentException("Range date inizio o durata invalide");
        }

        Hackathon newHackathon = new Hackathon(organizzatore, dati, giudice, iscrizioni, durata);

        // controllo duplicazione
        if(hackathons.contains(newHackathon)) {
            throw new IllegalArgumentException("Hackathon già presente nel sistema");
        }
        hackathons.add(newHackathon);
        return newHackathon;
    }
    
    private boolean validaDati(DatiHackathon datiHackathon) {
        // TODO: usare Jakarta Validator
        return true;
    }

    /**
     * Esegue la transizione di stato dell'hackathon alla fase successiva.
     * La logica specifica è delegata allo stato corrente dell'hackathon (State Pattern).
     *
     * @param hackathon L'hackathon da far avanzare.
     */
    public void avanzaStato(@NonNull Hackathon hackathon) {
        hackathon.nextState();
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
        return hackathons.stream()
                .filter(predicate)
                .collect(Collectors.toSet());
    }

    public void chiudiSottomissioni(UUID id) {
        Hackathon hackathon = getHackathon(id);
        if(hackathon.getRappresentazioneStato() == RappresentazioneStato.IN_CORSO) {
            avanzaStato(hackathon);
        }
    }

    /**
     * Aggiunge un singolo mentore all'hackathon.
     * Verifica che il mentore non sia già presente e delega allo stato corrente
     * la validazione dell'operazione.
     *
     * @param hackathon L'hackathon di destinazione.
     * @param mentore   Il mentore da aggiungere.
     * @throws IllegalArgumentException Se il mentore è già assegnato all'hackathon.
     */
    private void aggiungiMentore(@NonNull Hackathon hackathon, @NonNull Mentore mentore) {
        if (hackathon.getMentori().contains(mentore)) {
            throw new IllegalArgumentException("Mentore già presente");
        }
        hackathon.aggiungiMentore(mentore);
    }

    /**
     * Aggiunge una collezione di mentori all'hackathon invocando l'aggiunta singola per ognuno.
     *
     * @param hackathon L'hackathon di destinazione.
     * @param mentori   La collezione di mentori da aggiungere.
     */
    public void aggiungiMentori(@NonNull Hackathon hackathon, @NonNull Collection<Mentore> mentori) {
        mentori.forEach(m -> this.aggiungiMentore(hackathon, m));
    }

    public void iscriviTeam(@NonNull Hackathon hackathon, @NonNull Utente utente) {
        if(!utente.haTeam()) {
            throw new IllegalArgumentException("Per iscriversi ad un Hackathon l'Utente deve avere un team");
        }
        Team team = utente.getTeam();
        if (hackathon.getIscritti().containsKey(team)) {
            throw new IllegalArgumentException("Team già iscritto");
        }
        if (hackathon.getDatiHackathon().dimensioneMaxTeam() < team.getMembri().size()) {
            throw new IllegalArgumentException("Team troppo grande");
        }
        hackathon.iscriviTeam(team);
    }
}