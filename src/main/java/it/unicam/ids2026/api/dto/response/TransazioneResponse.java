package it.unicam.ids2026.api.dto.response;

import it.unicam.ids2026.core.transaction.Transaction;
import it.unicam.ids2026.core.transaction.TransactionStatus;
import it.unicam.ids2026.core.transaction.MoneyAmount;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Response DTO per una transazione di pagamento.
 */
public record TransazioneResponse(
        UUID idTransazione,
        String nomeTeamBeneficiario,
        UUID teamBeneficiarioId,
        MoneyAmount importo,
        String stato,
        LocalDateTime dataDiEmissione
) {
    /**
     * Crea un TransazioneResponse da una Transaction.
     *
     * @param transaction la transazione
     * @return TransazioneResponse
     */
    public static TransazioneResponse from(Transaction transaction) {
        return new TransazioneResponse(
                UUID.randomUUID(), // TODO: Transaction dovrebbe avere un ID proprio
                transaction.getBeneficiario().getNome(),
                transaction.getBeneficiario().getId(),
                transaction.getImporto(),
                transaction.getStato().name(),
                transaction.getDataDiEmissione()
        );
    }
}
