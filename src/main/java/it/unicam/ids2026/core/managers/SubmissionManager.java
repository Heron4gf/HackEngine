package it.unicam.ids2026.core.managers;

import it.unicam.ids2026.core.hackathon.Hackathon;
import it.unicam.ids2026.core.hackathon.data.Sottomissione;
import it.unicam.ids2026.core.hackathon.data.Valutazione;
import it.unicam.ids2026.core.roles.team.Team;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.io.File;
import java.util.Map;
import java.util.NoSuchElementException;

@RequiredArgsConstructor
public class SubmissionManager {

    /**
     * Metodo che crea una nuova sottomissione e la registra nel sistema.
     * Se il team aveva già una sottomissione, questa viene sovrascritta.
     *
     * @param hackathon   l'hackathon a cui appartiene la sottomissione
     * @param nome        il nome della sottomissione
     * @param descrizione la descrizione del progetto
     * @param allegato    il file contenente l'allegato da consegnare
     * @param team        il team che effettua la sottomissione
     * @throws IllegalArgumentException se i dati della sottomissione non sono validi
     */
    public void inviaSottomissione(@NonNull Hackathon hackathon,
                                   @NonNull String nome,
                                   @NonNull String descrizione,
                                   @NonNull File allegato,
                                   @NonNull Team team) {
        if (!validaSottomissione(nome, descrizione, allegato)) {
            throw new IllegalArgumentException("I dati della sottomissione non sono validi.");
        }

        Sottomissione newSubmission = new Sottomissione(nome, descrizione, allegato);
        hackathon.aggiungiSottomissione(team, newSubmission);
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
        if (!hackathon.getIscritti().containsKey(team)) {
            throw new NoSuchElementException("Il team non risulta iscritto all'hackathon.");
        }

        Sottomissione sottomissione = hackathon.getIscritti().get(team).getSottomissione();

        if (sottomissione == null) {
            throw new NoSuchElementException("Nessuna sottomissione trovata per il team specificato.");
        }

        return sottomissione;
    }

    /**
     * Restituisce tutte le sottomissioni dell'hackathon.
     *
     * @param h l'hackathon di cui ottenere le sottomissioni
     * @return la mappa team-sottomissione associata all'hackathon
     */
    public Map<Team, Sottomissione> ottieniSottomissioni(@NonNull Hackathon h) {
        return h.getSottomissioni();
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

    /**
     * Verifica se i parametri per istanziare la sottomissione sono validi.
     *
     * @param nome        il nome della sottomissione
     * @param descrizione la descrizione della sottomissione
     * @param allegato    il file allegato
     * @return true se i dati sono validi, false altrimenti
     */
    public boolean validaSottomissione(@NonNull String nome,
                                       @NonNull String descrizione,
                                       @NonNull File allegato) {
        return !nome.isBlank()
                && !descrizione.isBlank()
                && allegato.exists()
                && allegato.isFile();
    }

    /**
     * Verifica se i parametri della valutazione sono validi.
     *
     * @param voto     il voto della valutazione
     * @param giudizio il giudizio della valutazione
     * @return true se la valutazione è valida, false altrimenti
     */
    private boolean validaValutazione(int voto, @NonNull String giudizio) {
        return voto >= 0
                && voto <= 10
                && !giudizio.isBlank()
                && giudizio.length() >= 3
                && giudizio.length() <= 200;
    }
}