package it.unicam.ids2026.core.hackathon.wallet;

import it.unicam.ids2026.core.transaction.MoneyAmount;
import it.unicam.ids2026.core.transaction.Transaction;
import it.unicam.ids2026.core.transaction.TransactionType;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NonNull;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Currency;
import java.util.List;

@Getter
public class HackathonWallet {

    private MoneyAmount saldo;

    @Getter(AccessLevel.NONE)
    private final List<Transaction> transazioni = new ArrayList<>();

    public HackathonWallet(@NonNull Currency currency) {
        this.saldo = new MoneyAmount(BigDecimal.ZERO, currency);
    }

    public HackathonWallet(@NonNull MoneyAmount saldo) {
        this.saldo = saldo;
    }

    public void aggiungiTransazione(@NonNull Transaction transazione) {
        this.transazioni.add(transazione);
        aggiornaSaldo(transazione);
    }

    private void aggiornaSaldo(@NonNull Transaction transazione) {
        MoneyAmount importo = transazione.getImporto();
        
        if (!saldo.getCurrency().equals(importo.getCurrency())) {
            throw new IllegalArgumentException("Currency mismatch");
        }
        
        if (transazione.getTipo() == TransactionType.CREDITO) {
            saldo = new MoneyAmount(
                saldo.getAmount().add(importo.getAmount()),
                saldo.getCurrency()
            );
        } else {
            saldo = new MoneyAmount(
                saldo.getAmount().subtract(importo.getAmount()),
                saldo.getCurrency()
            );
        }
    }

    public List<Transaction> getTransazioni() {
        return List.copyOf(transazioni);
    }
}