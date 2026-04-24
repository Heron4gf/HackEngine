package it.unicam.ids2026.api.dto.response;

import it.unicam.ids2026.core.roles.User;
import it.unicam.ids2026.core.roles.staff.MembroStaff;

import java.util.UUID;

public record UserResponse(
        UUID id,
        String nome,
        String cognome,
        String role
) {
    public static UserResponse from(User user) {
        String cognome = user instanceof MembroStaff membroStaff ? membroStaff.getCognome() : null;
        return new UserResponse(
                user.getId(),
                user.getNome(),
                cognome,
                user.getClass().getSimpleName()
        );
    }
}
