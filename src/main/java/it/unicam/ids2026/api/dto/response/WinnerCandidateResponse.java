package it.unicam.ids2026.api.dto.response;

import it.unicam.ids2026.core.roles.team.Iscrizione;
import it.unicam.ids2026.core.roles.team.Team;

public record WinnerCandidateResponse(
        String teamId,
        String teamName,
        double punteggio
) {
    public static WinnerCandidateResponse from(Team team, Iscrizione iscrizione) {
        return new WinnerCandidateResponse(
                team.getId().toString(),
                team.getNome(),
                iscrizione.getSottomissione().getValutazione().voto()
        );
    }
}
