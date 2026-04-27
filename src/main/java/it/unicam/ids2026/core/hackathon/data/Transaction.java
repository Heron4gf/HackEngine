package it.unicam.ids2026.core.hackathon.data;

import it.unicam.ids2026.api.external.payments.IPaymentMethod;
import it.unicam.ids2026.core.roles.User;
import it.unicam.ids2026.core.roles.team.Team;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.converters.models.MonetaryAmount;

import java.time.LocalDateTime;

@Getter
@RequiredArgsConstructor
public class Transaction {
    private final MonetaryAmount importo;
    private final IPaymentMethod metodo;
    private final User ordinante;
    private final Team beneficiario;

    private TransactionStatus stato = TransactionStatus.PROCESSANDO;
    private final LocalDateTime dataDiEmissione = LocalDateTime.now();

    public void processa() {
        try {
            metodo.pay(ordinante, beneficiario, importo);
            stato = TransactionStatus.SUCCESSO;
        } catch (Exception exception) {
            exception.printStackTrace();
            stato = TransactionStatus.FALLITO;
        }
    }

}
