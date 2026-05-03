package it.unicam.ids2026.api.dto.response;

import it.unicam.ids2026.core.roles.team.Team;

import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

public record TeamResponse(
        UUID id,
        String nome,
        int maxMembri,
        int membriAttuali,
        Set<UUID> membriIds
) {
    public static TeamResponse from(Team team) {
        return new TeamResponse(
                team.getId(),
                team.getNome(),
                team.getMaxMembri(),
                team.getMembri().size(),
                team.getMembri().stream()
                        .map(it.unicam.ids2026.core.roles.User::getId)
                        .collect(Collectors.toSet())
        );
    }
}
