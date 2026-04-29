package it.unicam.ids2026.core.transaction;

/**
 * Interfaccia che rappresenta una parte coinvolta in una transazione di pagamento.
 * Implementata da Hackathon (come pagatore) e Team (come beneficiario).
 */
public interface IParteDiPagamento {

    /**
     * Restituisce i dettagli identificativi della parte.
     *
     * @return una stringa con i dettagli del conto
     */
    String dettagliConto();
}
