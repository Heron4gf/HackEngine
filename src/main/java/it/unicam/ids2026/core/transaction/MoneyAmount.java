package it.unicam.ids2026.core.transaction;

import lombok.NonNull;

import java.math.BigDecimal;
import java.util.Currency;

public record MoneyAmount(@NonNull BigDecimal amount, @NonNull Currency currency) {
}
