package it.unicam.ids2026.api.dto.request;

import jakarta.validation.constraints.NotNull;

import java.util.UUID;

/**
 * Request DTO per l'elaborazione del pagamento del premio.
 */
public record ElaboraPagamentoRequest(
        @NotNull(message = "L'ID del team vincitore è obbligatorio")
        String nomeTeamVincitore
) {}
