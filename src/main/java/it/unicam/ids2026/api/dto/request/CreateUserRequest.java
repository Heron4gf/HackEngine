package it.unicam.ids2026.api.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record CreateUserRequest(
        @NotNull(message = "Il ruolo e obbligatorio")
        UserRole role,

        @NotBlank(message = "Il nome non puo essere vuoto")
        @Size(min = 3, max = 30, message = "Il nome deve avere tra 3 e 30 caratteri")
        String nome,

        @Size(min = 3, max = 30, message = "Il cognome deve avere tra 3 e 30 caratteri")
        String cognome
) {
    public enum UserRole {
        UTENTE,
        ORGANIZZATORE,
        GIUDICE,
        MENTORE
    }
}
