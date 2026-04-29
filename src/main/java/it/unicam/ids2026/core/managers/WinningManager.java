package it.unicam.ids2026.core.managers;

import it.unicam.ids2026.core.hackathon.Hackathon;
import it.unicam.ids2026.core.hackathon.status.RappresentazioneStato;
import it.unicam.ids2026.core.roles.team.Iscrizione;
import it.unicam.ids2026.core.roles.team.Team;
import it.unicam.ids2026.core.transaction.MoneyAmount;
import it.unicam.ids2026.core.transaction.Transaction;
import it.unicam.ids2026.core.transaction.factory.TransactionFactory;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class WinningManager {

    private final TransactionFactory transactionFactory;
    private final HackathonManager hackathonManager;

    @Autowired
    public WinningManager(TransactionFactory transactionFactory, HackathonManager hackathonManager) {
        this.transactionFactory = transactionFactory;
        this.hackathonManager = hackathonManager;
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

        // Use the prize from DatiHackathon (configured prize) for the payment
        MoneyAmount premio = hackathon.getDatiHackathon().premioInDenaro();

        // L'hackathon effettua il pagamento al team vincitore
        Transaction transazione = transactionFactory.makePayment(hackathon, vincitore, premio);
        
        // Register transaction in the hackathon wallet
        hackathon.getWallet().aggiungiTransazione(transazione);
        
        return transazione;
    }

    /**
     * @return true se tutte le sottomissioni siano state valutate, false altrimenti
     */
    public boolean controllaSeValutate(@NonNull Hackathon hackathon) {
        return
                hackathon.getIscritti().
                        entrySet().
                        stream().
                        filter(entry -> !entry.
                                getValue().
                                hasValutazione())
                        .collect(Collectors.toSet()).isEmpty();
    }

    /**
     * Restituisce l'elenco delle iscrizioni che hanno ottenuto il punteggio massimo.
     * Se più team hanno lo stesso punteggio primo in classifica, vengono restituiti tutti.
     */
    public List<Iscrizione> ottieniTeamConPunteggioMassimo(@NonNull Hackathon hackathon) {
        if(!controllaSeValutate(hackathon)) {
            throw new IllegalArgumentException("L'hackathon in questione ha sottomissioni non valutate");
        }

        double maxPunteggio = hackathon.getIscritti().values().stream()
                .filter(Iscrizione::hasValutazione)
                .mapToDouble(i -> i.getSottomissione().getValutazione().voto())
                .max()
                .orElse(Double.NEGATIVE_INFINITY);

        return hackathon.getIscritti().values().stream()
                .filter(i -> i.hasValutazione() &&
                        Double.compare(i.getSottomissione().getValutazione().voto(), maxPunteggio) == 0)
                .toList();
    }

    /**
     * Assegna il vincitore all'hackathon.
     * @throws IllegalArgumentException se non tutte le sottomissioni sono valutate
     */
    public void assegnaVincitore(@NonNull Hackathon hackathon, @NonNull Team vincitore) {
        if (!controllaSeValutate(hackathon)) {
            throw new IllegalArgumentException("Ci sono sottomissioni non ancora valutate");
        }
        hackathon.assegnaVincitore(vincitore);
        hackathonManager.avanzaStato(hackathon);
    }

    /**
     * Verifica se il vincitore ha già ricevuto il pagamento.
     *
     * @param hackathon l'hackathon di riferimento
     * @return true se il team ha già ricevuto il pagamento, false altrimenti
     * @throws IllegalArgumentException se il team non è il vincitore assegnato
     */
    public boolean vincitoreHaGiaRicevutoPremio(@NonNull Hackathon hackathon) {
        if(
                hackathon.getVincitore() == null
                        || !hackathon.getRappresentazioneStato().equals(RappresentazioneStato.CONCLUSO)
        ) {
            throw new IllegalArgumentException("L'Hackathon non ha ancora un vincitore");
        }
        return hackathon.getTransazioniPremio().stream()
                .anyMatch(Transaction::isSuccessful);
    }
}
