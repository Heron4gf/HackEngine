package it.unicam.ids2026.core.transaction.factory;

import it.unicam.ids2026.core.transaction.IBankAccount;
import it.unicam.ids2026.core.transaction.Transaction;
import it.unicam.ids2026.core.transaction.MoneyAmount;

public interface TransactionFactory {
    /**
     * Crea ed esegue una transazione di pagamento.
     *
     * @param ordinante   l'entità che effettua il pagamento
     * @param beneficiario il team che riceve il pagamento
     * @param amount      l'importo da pagare
     * @return la transazione creata con lo stato aggiornato (SUCCESSO o FALLITO)
     */
    Transaction makePayment(IBankAccount ordinante, IBankAccount beneficiario, MoneyAmount amount);
}
