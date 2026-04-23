package it.unicam.ids2026.core.managers;

import it.unicam.ids2026.core.hackathon.Hackathon;
import it.unicam.ids2026.core.hackathon.data.Sottomissione;
import it.unicam.ids2026.core.roles.team.Team;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.io.File;

@RequiredArgsConstructor
public class SubmissionManager {

    /**
     * Metodo che crea una nuova sottomissione e la regsitra nel sistema
     * @param nome  il nome della sottomissione
     * @param descrizione   la descrizione del progetto
     * @param allegato  il file contenente l'allegato da consegnare
     * @param team  il team di cui si vuole registrare la sottomissione
     * @return      la sottomissione appena istanziata
     */
    public Sottomissione inviaSottomissione(@NonNull String nome,@NonNull String descrizione,@NonNull File allegato,
                                           @NonNull Team team, @NonNull Hackathon hackathon) throws Exception {
        if (!validaSottomissione(nome, descrizione, allegato)) {
            throw new Exception();
        }
        Sottomissione newSubmission = new Sottomissione(nome, descrizione, allegato);
        hackathon.aggiungiSottomissione(team, newSubmission);
        return newSubmission;
    }

    /**
     *
     * @param team  il team
     * @return  la sottmissione trovata per quel team
     */
    public Sottomissione ottieniSottomissione(Hackathon hackathon, Team team) {
        return hackathon.getIscritti().get(team).getSottomissione();
    }

    /**
     * Metodo privato che verifica se i parametri per istanziare la sottomissione sono validi
     * @param nome  il nome della sottomissione
     * @param descrizione   la descrizione della sottomissione
     * @param allegato  il file che contiene l'allegato della sottomissione
     * @return true se i dati sono validi, false altrimenti
     */
    private boolean validaSottomissione(@NonNull String nome, @NonNull String descrizione,
                                        @NonNull File allegato) {
        return !nome.isEmpty() && !descrizione.isEmpty() && allegato.getTotalSpace() != 0L;
    }
}
