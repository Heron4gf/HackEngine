package it.unicam.ids2026.core.transaction.factory;

import it.unicam.ids2026.api.external.payments.IPaymentMethod;
import it.unicam.ids2026.core.roles.User;
import it.unicam.ids2026.core.roles.team.Team;
import it.unicam.ids2026.core.transaction.Transaction;
import it.unicam.ids2026.core.transaction.TransactionStatus;
import it.unicam.ids2026.core.transaction.MoneyAmount;

public abstract class AbstractTransactionFactory implements TransactionFactory {

    @Override
    public Transaction makePayment(User user, Team team, MoneyAmount amount) {
        Transaction transaction = buildTransaction(user, team, amount);

        try {
            transaction.getMetodo().pay(user, team, amount);
            transaction.setStato(TransactionStatus.SUCCESSO);
        } catch (Exception exception) {
            exception.printStackTrace();
            transaction.setStato(TransactionStatus.FALLITO);
        }
        return transaction;
    }

    protected Transaction buildTransaction(User user, Team team, MoneyAmount amount) {
        return new Transaction(amount, buildPaymentMethod(), user, team);
    }

    protected abstract IPaymentMethod buildPaymentMethod();
}
