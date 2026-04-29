package it.unicam.ids2026.api.dto.request;

import jakarta.validation.constraints.NotBlank;

/**
 * Request DTO per l'assegnazione del vincitore all'hackathon.
 */
public record AssegnaVincitoreRequest(
        /**
         * L'ID del team da assegnare come vincitore.
         */
        @NotBlank(message = "L'ID del team è obbligatorio")
        String teamId
) {}
