package it.unicam.ids2026.api.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.UUID;

public record CreateSupportRequest(
        @NotNull(message = "Il team e obbligatorio")
        UUID teamId,

        @NotBlank(message = "Il titolo non puo essere vuoto")
        @Size(min = 3, max = 100, message = "Il titolo deve essere tra 3 e 100 caratteri")
        String titolo,

        @NotBlank(message = "La descrizione non puo essere vuota")
        @Size(min = 10, max = 1000, message = "La descrizione deve essere tra 10 e 1000 caratteri")
        String descrizione
) {}
