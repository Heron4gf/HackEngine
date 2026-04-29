package it.unicam.ids2026.core.transaction.factory;

import it.unicam.ids2026.api.external.payments.DefaultPaymentMethod;
import it.unicam.ids2026.api.external.payments.IPaymentMethod;
import org.springframework.stereotype.Component;

/**
 * Factory predefinita per la creazione di transazioni.
 */
@Component
public class DefaultTransactionFactory extends AbstractTransactionFactory {
    @Override
    protected IPaymentMethod buildPaymentMethod() {
        return new DefaultPaymentMethod();
    }
}
