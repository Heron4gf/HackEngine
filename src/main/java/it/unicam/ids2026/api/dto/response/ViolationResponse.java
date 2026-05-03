package it.unicam.ids2026.api.dto.response;

import it.unicam.ids2026.core.violation.Violazione;

import java.util.UUID;

public record ViolationResponse(
        UUID id,
        UUID hackathonId,
        UUID mentoreId,
        UUID teamId,
        String descrizione,
        String stato
) {
    public static ViolationResponse from(Violazione violazione) {
        return new ViolationResponse(
                violazione.getId(),
                violazione.getHackathon().getId(),
                violazione.getSegnalatoDa().getId(),
                violazione.getColpevole().getId(),
                violazione.getMotivazione(),
                violazione.getStato().name()
        );
    }
}
