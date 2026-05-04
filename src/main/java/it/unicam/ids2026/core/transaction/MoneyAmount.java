package it.unicam.ids2026.core.transaction;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;
import java.math.BigDecimal;
import java.util.Currency;

@Getter
@AllArgsConstructor
public class MoneyAmount {
    @NonNull private BigDecimal amount;
    @NonNull private final Currency currency;

    public void add(@NonNull MoneyAmount other) {
        if (!this.currency.equals(other.currency)) {
            throw new IllegalArgumentException("Can't add two different currencies");
        }
        this.amount = this.amount.add(other.amount);
    }
}