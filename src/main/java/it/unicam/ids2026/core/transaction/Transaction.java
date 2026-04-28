package it.unicam.ids2026.core.transaction;

import it.unicam.ids2026.api.external.payments.IPaymentMethod;
import it.unicam.ids2026.core.roles.User;
import it.unicam.ids2026.core.roles.team.Team;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import it.unicam.ids2026.core.transaction.MoneyAmount;

import java.time.LocalDateTime;

@Getter
@RequiredArgsConstructor
public class Transaction {
    private final MoneyAmount importo;
    private final IPaymentMethod metodo;
    private final User ordinante;
    private final Team beneficiario;

    @Setter
    private TransactionStatus stato = TransactionStatus.PROCESSANDO;
    private final LocalDateTime dataDiEmissione = LocalDateTime.now();

}
