package it.unicam.ids2026.api.events;

import it.unicam.ids2026.core.roles.team.Team;
import lombok.NonNull;

@FunctionalInterface
public interface TeamDeletionListener extends Listener {

    void notifyTeamDeletion(@NonNull Team team);
}
