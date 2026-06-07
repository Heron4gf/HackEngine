package it.unicam.ids2026.api.dto.response;

import java.util.UUID;

import it.unicam.ids2026.core.roles.team.Invito;

public record InvitoResponse(
        String nomeTeamMittente,
        UUID destinatarioId
) {
    public static InvitoResponse from(Invito invito) {
        return new InvitoResponse(
                invito.getMittente().getNome(),
                invito.getDestinatario().getId()
        );
    }
}