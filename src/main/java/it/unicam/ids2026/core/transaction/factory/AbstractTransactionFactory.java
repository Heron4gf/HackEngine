package it.unicam.ids2026.core.transaction.factory;

import it.unicam.ids2026.api.external.payments.IPaymentMethod;
import it.unicam.ids2026.core.transaction.*;

public abstract class AbstractTransactionFactory implements TransactionFactory {

    @Override
    public Transaction makePayment(IParteDiPagamento ordinante, IParteDiPagamento beneficiario, MoneyAmount amount) {
        Transaction transaction = buildTransaction(ordinante, beneficiario, amount);

        try {
            transaction.getMetodo().pay(ordinante, beneficiario, amount);
            transaction.setStato(TransactionStatus.SUCCESSO);
        } catch (Exception exception) {
            exception.printStackTrace();
            transaction.setStato(TransactionStatus.FALLITO);
        }
        return transaction;
    }

    protected Transaction buildTransaction(IParteDiPagamento ordinante, IParteDiPagamento beneficiario, MoneyAmount amount) {
        return new Transaction(amount, buildPaymentMethod(), ordinante, beneficiario);
    }

    protected abstract IPaymentMethod buildPaymentMethod();
}
