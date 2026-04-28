package it.unicam.ids2026.core.transaction.factory;

import it.unicam.ids2026.api.external.payments.IPaymentMethod;
import it.unicam.ids2026.core.transaction.IBankAccount;
import it.unicam.ids2026.core.transaction.Transaction;
import it.unicam.ids2026.core.transaction.TransactionStatus;
import it.unicam.ids2026.core.transaction.MoneyAmount;

public abstract class AbstractTransactionFactory implements TransactionFactory {

    @Override
    public Transaction makePayment(IBankAccount ordinante, IBankAccount beneficiario, MoneyAmount amount) {
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

    protected Transaction buildTransaction(IBankAccount ordinante, IBankAccount beneficiario, MoneyAmount amount) {
        return new Transaction(amount, buildPaymentMethod(), ordinante, beneficiario);
    }

    protected abstract IPaymentMethod buildPaymentMethod();
}
