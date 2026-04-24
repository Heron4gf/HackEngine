package it.unicam.ids2026.api.dto.request;

import jakarta.validation.constraints.*;
import lombok.NonNull;

import java.util.UUID;

public record InviteUserRequest(
        @NotNull(message = "Il team mittente è obbligatorio")
        String teamMittenteNome,

        @NotNull(message = "L'utente destinatario è obbligatorio")
        UUID destinatarioId
) {}
