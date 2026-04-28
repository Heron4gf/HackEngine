package it.unicam.ids2026.core.transaction.factory;

import it.unicam.ids2026.core.roles.User;
import it.unicam.ids2026.core.roles.team.Team;
import it.unicam.ids2026.core.transaction.Transaction;
import it.unicam.ids2026.core.transaction.MoneyAmount;

public interface TransactionFactory {
    /**
     * Crea ed esegue una transazione di pagamento.
     *
     * @param user   l'utente che effettua il pagamento (ordinante)
     * @param team   il team che riceve il pagamento (beneficiario)
     * @param amount l'importo da pagare
     * @return la transazione creata con lo stato aggiornato (SUCCESSO o FALLITO)
     */
    Transaction makePayment(User user, Team team, MoneyAmount amount);
}
