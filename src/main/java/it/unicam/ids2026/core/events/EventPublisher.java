package it.unicam.ids2026.core.events;

import it.unicam.ids2026.api.events.DeletionListener;
import it.unicam.ids2026.api.events.Listener;
import it.unicam.ids2026.api.events.UserChangeListener;
import it.unicam.ids2026.core.roles.team.Team;
import it.unicam.ids2026.core.roles.team.Utente;
import lombok.NonNull;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.LinkedList;

@Component
public class EventPublisher {

    private final Collection<Listener> listeners = new LinkedList<>();


    public EventPublisher() {
    }

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

    private <T extends Listener> Collection<T> getListenersOfType(Class<T> type) {
        return listeners.stream()
                .filter(type::isInstance)
                .map(type::cast)
                .toList();
    }


    /**
     * @param team
     */
    public void publishDeletion(@NonNull Team team) {
        getListenersOfType(DeletionListener.class)
                .forEach(listener -> listener.notifyDeletion(team));
    }

    public void publishUserChange(@NonNull Utente utente) {
        getListenersOfType(UserChangeListener.class)
                .forEach(listener -> listener.notifyUserChange(utente));
    }

}
