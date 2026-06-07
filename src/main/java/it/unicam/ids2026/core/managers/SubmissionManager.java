package it.unicam.ids2026.core.managers;

import it.unicam.ids2026.core.hackathon.Hackathon;
import it.unicam.ids2026.core.hackathon.data.Sottomissione;
import it.unicam.ids2026.core.hackathon.data.Valutazione;
import it.unicam.ids2026.core.roles.team.Team;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import org.springframework.stereotype.Service;

import java.io.File;
import java.nio.file.Paths;
import java.util.NoSuchElementException;

@Service
@NoArgsConstructor
public class SubmissionManager {

    /**
     * Metodo che crea una nuova sottomissione e la registra nel sistema.
     * Se il team aveva già una sottomissione, viene lanciata un'eccezione
     *
     * @param hackathon   l'hackathon a cui appartiene la sottomissione
     * @param nome        il nome della sottomissione
     * @param descrizione la descrizione del progetto
     * @param allegato    il file contenente l'allegato da consegnare
     * @param team        il team che effettua la sottomissione
     * @throws IllegalArgumentException se i dati della sottomissione non sono validi
     */
    public Sottomissione inviaSottomissione(@NonNull Hackathon hackathon,
                                   @NonNull Team team,
                                   @NonNull String nome,
                                   @NonNull String descrizione,
                                   @NonNull File allegato
                                   ) {
        if(ottieniSottomissione(hackathon, team) != null) {
            throw new IllegalArgumentException("La sottomissione esiste già");
        }

        if (!validaSottomissione(nome, descrizione, allegato)) {
            throw new IllegalArgumentException("I dati della sottomissione non sono validi.");
        }

        Sottomissione newSubmission = new Sottomissione(nome, descrizione, allegato);
        hackathon.aggiungiSottomissione(team, newSubmission);
        return newSubmission;
    }

    /**
     * Aggiorna la sottomissione del {@link Team} specificato all'interno
     * dell'hackathon indicato.
     *
     * <p>La sottomissione corrente viene recuperata dall'hackathon e ne viene
     * mantenuto il nome originale. I nuovi dati (descrizione e allegato) vengono
     * validati tramite {@code validaSottomissione}. Se i dati non risultano validi,
     * viene sollevata una {@link IllegalArgumentException}.</p>
     *
     * <p>In caso di validazione positiva, viene creata una nuova istanza di
     * {@link Sottomissione} che sostituisce quella precedente tramite
     * {@code hackathon.setSottomissione}.</p>
     *
     * @param hackathon   l'hackathon a cui appartiene la sottomissione; non deve essere {@code null}
     * @param team        il team di cui aggiornare la sottomissione; non deve essere {@code null}
     * @param descrizione la nuova descrizione della sottomissione; non deve essere {@code null}
     * @param allegato    il nuovo file allegato alla sottomissione; non deve essere {@code null}
     * @return la nuova sottomissione aggiornata
     * @throws IllegalArgumentException se i dati forniti non superano la validazione
     */
    public Sottomissione aggiornaSottomissione(@NonNull Hackathon hackathon,
                                               @NonNull Team team,
                                               @NonNull String descrizione,
                                               @NonNull File allegato) {
        Sottomissione sottomissioneCorrente = hackathon.getSottomissione(team);
        String nome = sottomissioneCorrente.getNome();

        if (!validaSottomissione(nome, descrizione, allegato)) {
            throw new IllegalArgumentException("I dati della sottomissione non sono validi.");
        }

        Sottomissione nuovaSottomissione = new Sottomissione(nome, descrizione, allegato);
        hackathon.aggiungiSottomissione(team, nuovaSottomissione);
        return nuovaSottomissione;
    }

    /**
     * Restituisce una mappa contenente tutte le sottomissioni presenti nell'hackathon specificato,
     * associando ciascun team alla propria sottomissione, filtrando i team che non hanno ancora consegnato.
     *
     * @param hackathon l'hackathon da cui recuperare le sottomissioni; non deve essere {@code null}
     * @return una mappa che associa ogni {@link Team} alla rispettiva {@link Sottomissione}
     */
    public java.util.Map<Team, Sottomissione> ottieniSottomissioni(@NonNull Hackathon hackathon) {
        return hackathon.getIscritti().entrySet().stream()
                .filter(entry -> entry.getValue().getSottomissione() != null)
                .collect(java.util.stream.Collectors.toMap(
                        java.util.Map.Entry::getKey,
                        entry -> entry.getValue().getSottomissione()
                ));
    }


    /**
     * Restituisce la sottomissione associata al team nell'hackathon indicato.
     *
     * @param hackathon l'hackathon in cui cercare la sottomissione
     * @param team      il team di cui ottenere la sottomissione
     * @return la sottomissione del team
     * @throws NoSuchElementException se il team non è iscritto o non ha una sottomissione
     */
    public Sottomissione ottieniSottomissione(@NonNull Hackathon hackathon, @NonNull Team team) {
        return hackathon.getSottomissione(team);
    }

    /**
     * Assegna una valutazione a una sottomissione.
     *
     * @param s        la sottomissione da valutare
     * @param voto     il voto della valutazione
     * @param giudizio il giudizio testuale
     * @throws IllegalArgumentException se voto o giudizio non sono validi
     */
    public void assegnaValutazione(@NonNull Sottomissione s, int voto, @NonNull String giudizio) {
        if (!validaValutazione(voto, giudizio)) {
            throw new IllegalArgumentException("La valutazione non è valida.");
        }

        s.setValutazione(new Valutazione(voto, giudizio));
    }

    private boolean validaSottomissione(@NonNull String nome,
                                       @NonNull String descrizione,
                                       @NonNull File allegato) {
        try {
            Paths.get(allegato.toURI());
        } catch (IllegalArgumentException ignored) {
            return false;
        }
        return !nome.isBlank()
                && !descrizione.isBlank();
    }

    private boolean validaValutazione(int voto, @NonNull String giudizio) {
        return voto >= 0
                && voto <= 10
                && !giudizio.isBlank()
                && giudizio.length() >= 3
                && giudizio.length() <= 200;
    }

}