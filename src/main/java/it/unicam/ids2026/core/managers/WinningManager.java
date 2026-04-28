package it.unicam.ids2026.core.managers;

import it.unicam.ids2026.core.hackathon.Hackathon;
import it.unicam.ids2026.core.hackathon.data.Sottomissione;
import it.unicam.ids2026.core.hackathon.data.Valutazione;
import it.unicam.ids2026.core.roles.User;
import it.unicam.ids2026.core.roles.team.Team;
import it.unicam.ids2026.core.transaction.MoneyAmount;
import it.unicam.ids2026.core.transaction.Transaction;
import it.unicam.ids2026.core.transaction.factory.TransactionFactory;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * Gestisce la determinazione del vincitore e l'elaborazione del pagamento del premio.
 */
@Service
public class WinningManager {

    private final SubmissionManager submissionManager;
    private final HackathonManager hackathonManager;
    private final TransactionFactory transactionFactory;

    @Autowired
    public WinningManager(SubmissionManager submissionManager,
                          HackathonManager hackathonManager,
                          TransactionFactory transactionFactory) {
        this.submissionManager = submissionManager;
        this.hackathonManager = hackathonManager;
        this.transactionFactory = transactionFactory;
    }


    /**
     * Determina il vincitore (o i vincitori in caso di parità).
     * Se più team hanno lo stesso punteggio massimo, restituisce tutti i team parimerito.
     *
     * @param hackathon l'hackathon di cui determinare il vincitore
     * @return lista di team parimerito per il primo posto
     * @throws IllegalStateException se non ci sono sottomissioni valutate
     */
    public List<Team> determinaVincitori(@NonNull Hackathon hackathon) {
        List<Map.Entry<Team, Integer>> classifica = ottieniClassifica(hackathon);

        if (classifica.isEmpty()) {
            throw new IllegalStateException("Non ci sono sottomissioni valutate per questo hackathon");
        }

        int punteggioMassimo = classifica.getFirst().getValue();

        return classifica.stream()
                .takeWhile(entry -> entry.getValue() == punteggioMassimo)
                .map(Map.Entry::getKey)
                .toList();
    }

    /**
     * Restituisce il voto di un team per un hackathon specifico.
     *
     * @param hackathon l'hackathon
     * @param team      il team di cui ottenere il voto
     * @return il voto del team, o null se non ha sottomesso o non è stato valutato
     */
    public Integer ottieniVotoTeam(@NonNull Hackathon hackathon, @NonNull Team team) {
        List<Map.Entry<Team, Integer>> classifica = ottieniClassifica(hackathon);
        return classifica.stream()
                .filter(entry -> entry.getKey().equals(team))
                .map(Map.Entry::getValue)
                .findFirst()
                .orElse(null);
    }

    /**
     * Elabora il pagamento del premio al team vincitore e lo registra nell'hackathon.
     *
     * @param hackathon l'hackathon di riferimento
     * @param vincitore il team vincitore che deve ricevere il premio
     * @return la transazione creata (con stato SUCCESSO o FALLITO)
     * @throws IllegalArgumentException se il team non è il vincitore assegnato
     */
    public Transaction elaboraPagamentoPremio(@NonNull Hackathon hackathon, @NonNull Team vincitore) {
        if (hackathon.getVincitore() == null || !hackathon.getVincitore().equals(vincitore)) {
            throw new IllegalArgumentException("Il team specificato non è il vincitore assegnato per questo hackathon");
        }

        User organizzatore = hackathon.getOrganizzatore();
        MoneyAmount premio = hackathon.getDatiHackathon().premioInDenaro();

        Transaction transazione = transactionFactory.makePayment(organizzatore, vincitore, premio);
        
        hackathon.aggiungiTransazionePremio(transazione);
        
        return transazione;
    }

    /**
     * Verifica se il vincitore ha già ricevuto il pagamento.
     *
     * @param hackathon l'hackathon di riferimento
     * @param team      il team da verificare
     * @return true se il team ha già ricevuto il pagamento, false altrimenti
     * @throws IllegalArgumentException se il team non è il vincitore assegnato
     */
    public boolean vincitoreHaGiaRicevutoPremio(@NonNull Hackathon hackathon, @NonNull Team team) {
        if (hackathon.getVincitore() == null || !hackathon.getVincitore().equals(team)) {
            throw new IllegalArgumentException("Il team specificato non è il vincitore assegnato per questo hackathon");
        }

        // TODO: Implementare con TransactionRepository quando disponibile
        // Per ora restituisce false come placeholder
        return false;
    }
}
