package it.unicam.ids2026.api.events;

import it.unicam.ids2026.core.roles.team.Team;

@FunctionalInterface
public interface DeletionListener {
    void notifyDeletion(Team team);
}
