package it.unicam.ids2026.core.events;

import it.unicam.ids2026.api.events.DeletionListener;
import it.unicam.ids2026.api.events.Listener;
import it.unicam.ids2026.core.roles.team.Team;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import java.util.Collection;
import java.util.LinkedList;

@AllArgsConstructor
@NoArgsConstructor
public class EventPublisher {
    @NonNull private Collection<Listener> listeners = new LinkedList<>();

    /**
     * @param listener
     */
    public void registerListener(@NonNull Listener listener) {
        listeners.add(listener);
    }

    /**
     * @param listener
     */
    public void deleteListener(@NonNull Listener listener) {
        listeners.remove(listener);
    }

    /**
     * @param team
     */
    public void publishDeletion(@NonNull Team team) {
        listeners.stream()
                .filter(DeletionListener.class::isInstance)
                .map(DeletionListener.class::cast)
                .forEach(listener -> listener.notifyDeletion(team));
    }

}