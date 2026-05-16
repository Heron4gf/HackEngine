package it.unicam.ids2026.api.events;

import it.unicam.ids2026.core.roles.team.Team;
import it.unicam.ids2026.core.roles.team.Utente;
import lombok.NonNull;

public interface TeamJoinListener extends Listener {

    void notifyEnterTeam(@NonNull Team team, @NonNull Utente utente);

}
