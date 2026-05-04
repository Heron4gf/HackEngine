package it.unicam.ids2026.api.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CreateSubmissionRequest(
        @NotBlank(message = "Il nome non può essere vuoto")
        String name,

        @NotBlank(message = "La descrizione non puo essere vuota")
        @Size(min = 10, max = 200, message = "La descrizione deve avere tra 10 e 200 caratteri")
        String descrizione,

        @NotBlank(message = "Il percorso dell'allegato non puo essere vuoto")
        String allegato
) {

}
