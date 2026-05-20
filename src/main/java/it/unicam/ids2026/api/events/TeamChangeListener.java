package it.unicam.ids2026.api.events;

import it.unicam.ids2026.core.roles.team.Team;
import lombok.NonNull;

public interface TeamChangeListener extends Listener {
    void notifyTeamChange(@NonNull Team team);
}
