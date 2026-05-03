package it.unicam.ids2026.api.dto.response;

import it.unicam.ids2026.core.transaction.Transaction;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public record TransactionResponse(
        UUID id,
        BigDecimal importo,
        String currency,
        String stato,
        boolean successful,
        LocalDateTime dataDiEmissione
) {
    public static TransactionResponse from(Transaction transaction) {
        return new TransactionResponse(
                transaction.getId(),
                transaction.getImporto().getAmount(),
                transaction.getImporto().getCurrency().getCurrencyCode(),
                transaction.getStato().name(),
                transaction.isSuccessful(),
                transaction.getDataDiEmissione()
        );
    }
}
