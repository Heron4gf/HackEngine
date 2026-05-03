package it.unicam.ids2026.api.dto.response;

import it.unicam.ids2026.core.supportRequest.RichiestaSupporto;

public record SupportRequestResponse(
        String titolo,
        String descrizione,
        String stato
) {
    public static SupportRequestResponse from(RichiestaSupporto richiesta) {
        return new SupportRequestResponse(
                richiesta.getTitolo(),
                richiesta.getDescrizione(),
                richiesta.getStato().name()
        );
    }
}
