package it.unicam.ids2026.api.external.payments;

import it.unicam.ids2026.core.transaction.IParteDiPagamento;
import it.unicam.ids2026.core.transaction.MoneyAmount;

public interface IPaymentMethod {

    void pay(IParteDiPagamento sender, IParteDiPagamento beneficiario, MoneyAmount amount);

}
