package it.unicam.ids2026.api.dto.request;

import jakarta.validation.constraints.*;
import lombok.NonNull;

import java.util.UUID;

public record SubmitSupportRequest(
        @NotNull(message = "L'hackathon è obbligatorio")
        UUID hackathonId,

        @NotNull(message = "Il team è obbligatorio")
        UUID teamId,

        @NotBlank(message = "Il titolo non può essere vuoto")
        @Size(min = 3, max = 100, message = "Il titolo deve essere tra 3 e 100 caratteri")
        String titolo,

        @NotBlank(message = "La descrizione non può essere vuota")
        @Size(min = 10, max = 1000, message = "La descrizione deve essere tra 10 e 1000 caratteri")
        String descrizione
) {}
