package it.unicam.ids2026.api.external.payments;

import it.unicam.ids2026.core.roles.User;
import it.unicam.ids2026.core.roles.team.Team;
import it.unicam.ids2026.core.transaction.MoneyAmount;

public class DefaultPaymentMethod implements IPaymentMethod {
    @Override
    public void pay(User sender, Team team, MoneyAmount monetaryAmount) {
        System.out.println("Finto pagamento di "+monetaryAmount.toString()+" fatto a favore del team "+team.getNome());
    }
}
