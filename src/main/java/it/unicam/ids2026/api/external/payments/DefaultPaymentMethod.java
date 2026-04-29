package it.unicam.ids2026.api.external.payments;

import it.unicam.ids2026.core.transaction.IBankAccount;
import it.unicam.ids2026.core.transaction.MoneyAmount;

public class DefaultPaymentMethod implements IPaymentMethod {
    @Override
    public void pay(IBankAccount sender, IBankAccount beneficiario, MoneyAmount amount) {
        System.out.println("Finto pagamento di "+amount+" fatto a favore del "+beneficiario.dettagliConto()+" da parte di "+sender.dettagliConto());
    }
}
