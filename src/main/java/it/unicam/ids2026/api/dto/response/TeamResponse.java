package it.unicam.ids2026.api.dto.response;

import it.unicam.ids2026.core.roles.User;
import it.unicam.ids2026.core.roles.team.Team;

import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

public record TeamResponse(
        String nome,
        int maxMembri,
        int membriAttuali,
        Set<UUID> membriIds
) {
    public static TeamResponse from(Team team) {
        return new TeamResponse(
                team.getNome(),
                team.getMaxMembri(),
                team.getMembri().size(),
                team.getMembri().stream()
                        .map(User::getId)
                        .collect(Collectors.toSet())
        );
    }
}
