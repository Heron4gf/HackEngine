package it.unicam.ids2026.api.external.payments;

import it.unicam.ids2026.core.transaction.IBankAccount;
import it.unicam.ids2026.core.transaction.MoneyAmount;

public interface IPaymentMethod {

    void pay(IBankAccount sender, IBankAccount beneficiario, MoneyAmount amount);

}
