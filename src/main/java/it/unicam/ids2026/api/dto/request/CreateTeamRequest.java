package it.unicam.ids2026.api.dto.request;

import jakarta.validation.constraints.*;
import lombok.NonNull;

import java.util.UUID;

public record CreateTeamRequest(
        @NotNull(message = "L'utente è obbligatorio")
        UUID utenteId,

        @NotBlank(message = "Il nome del team non può essere vuoto")
        @Size(min = 2, max = 50, message = "Il nome deve essere tra 2 e 50 caratteri")
        String nome,

        @Min(value = 1, message = "Il team deve avere almeno 1 membro")
        @Max(value = 20, message = "Il team può avere al massimo 20 membri")
        int maxMembri
) {}
