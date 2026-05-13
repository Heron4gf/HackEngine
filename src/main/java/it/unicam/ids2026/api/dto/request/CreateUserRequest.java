package it.unicam.ids2026.api.dto.request;

import it.unicam.ids2026.core.roles.User;
import it.unicam.ids2026.core.roles.staff.Giudice;
import it.unicam.ids2026.core.roles.staff.Mentore;
import it.unicam.ids2026.core.roles.staff.Organizzatore;
import it.unicam.ids2026.core.roles.team.Utente;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

public record CreateUserRequest(
        @NotNull(message = "Il ruolo e obbligatorio")
        UserRole role,

        @NotBlank(message = "Il nome non puo essere vuoto")
        @Size(min = 3, max = 30, message = "Il nome deve avere tra 3 e 30 caratteri")
        String nome,

        @Size(min = 3, max = 30, message = "Il cognome deve avere tra 3 e 30 caratteri")
        String cognome
) {
    @RequiredArgsConstructor
    @Getter
    public enum UserRole {
        UTENTE(Utente.class),
        ORGANIZZATORE(Organizzatore.class),
        GIUDICE(Giudice.class),
        MENTORE(Mentore.class);

        private final Class<? extends User> roleClass;
    }
}
