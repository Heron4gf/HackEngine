package it.unicam.ids2026.core.transaction;

/**
 * Interfaccia che rappresenta un'entità in grado di ricevere pagamenti.
 * Implementata da Hackathon (come pagatore) e Team (come beneficiario).
 */
public interface IBankAccount {


    String dettagliConto();

    /**
     * Registra un pagamento ricevuto su questo conto.
     *
     * @param amount l'importo ricevuto
     */
    void riceviPagamento(MoneyAmount amount);
}
