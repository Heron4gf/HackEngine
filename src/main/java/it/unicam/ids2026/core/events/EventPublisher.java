package it.unicam.ids2026.core.events;

import it.unicam.ids2026.api.events.DeletionListener;
import it.unicam.ids2026.api.events.Listener;
import it.unicam.ids2026.core.roles.team.Team;
import lombok.NonNull;

import java.util.Collection;

public class EventPublisher {
    private Collection<Listener> listeners;

    public void registerListener(@NonNull Listener listener) {
        listeners.add(listener);
    }

    public void deleteListener(@NonNull Listener listener) {
        listeners.add(listener);
    }


    public void publishDeletion(@NonNull Team team) {
        listeners.stream()
                .filter(DeletionListener.class::isInstance)
                .map(DeletionListener.class::cast)
                .forEach(listener -> listener.notifyDeletion(team));
    }

}
