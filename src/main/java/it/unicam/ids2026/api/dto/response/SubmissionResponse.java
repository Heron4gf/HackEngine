package it.unicam.ids2026.api.dto.response;

import it.unicam.ids2026.core.hackathon.data.Sottomissione;

public record SubmissionResponse(
        String nome,
        String descrizione,
        String allegato,
        Integer voto,
        String giudizio
) {
    public static SubmissionResponse from(Sottomissione sottomissione) {
        return new SubmissionResponse(
                sottomissione.getNome(),
                sottomissione.getDescrizione(),
                sottomissione.getAllegato().getPath(),
                sottomissione.hasValutazione() ? sottomissione.getValutazione().voto() : null,
                sottomissione.hasValutazione() ? sottomissione.getValutazione().giudizio() : null
        );
    }
}
