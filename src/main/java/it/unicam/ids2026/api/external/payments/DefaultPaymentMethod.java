package it.unicam.ids2026.api.external.payments;

import it.unicam.ids2026.core.roles.User;
import it.unicam.ids2026.core.roles.team.Team;
import org.springdoc.core.converters.models.MonetaryAmount;

public class DefaultPaymentMethod implements IPaymentMethod {
    @Override
    public void pay(User sender, Team team, MonetaryAmount monetaryAmount) {
        System.out.println("Finto pagamento di "+monetaryAmount.toString()+" fatto a favore del team "+team.getNome());
    }
}
