package it.unicam.ids2026.api.external.payments;

import it.unicam.ids2026.core.roles.User;
import it.unicam.ids2026.core.roles.team.Team;
import it.unicam.ids2026.core.transaction.MoneyAmount;

public interface IPaymentMethod {

    void pay(User sender, Team team, MoneyAmount monetaryAmount);

}
