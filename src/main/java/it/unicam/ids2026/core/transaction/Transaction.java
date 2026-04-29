package it.unicam.ids2026.core.transaction;

import it.unicam.ids2026.api.external.payments.IPaymentMethod;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@RequiredArgsConstructor
public class Transaction {
    private final MoneyAmount importo;
    private final IPaymentMethod metodo;
    private final IBankAccount ordinante;
    private final IBankAccount beneficiario;

    @Setter
    private TransactionStatus stato = TransactionStatus.PROCESSANDO;
    private final LocalDateTime dataDiEmissione = LocalDateTime.now();

    public boolean isSuccessful() {
        return stato.equals(TransactionStatus.SUCCESSO);
    }

}
