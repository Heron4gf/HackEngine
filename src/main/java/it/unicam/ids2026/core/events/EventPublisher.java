package it.unicam.ids2026.core.events;

import it.unicam.ids2026.api.events.DeletionListener;
import it.unicam.ids2026.api.events.Listener;
import it.unicam.ids2026.api.events.TeamJoinListener;
import it.unicam.ids2026.core.roles.team.Team;
import it.unicam.ids2026.core.roles.team.Utente;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.LinkedList;

@Component
public class EventPublisher {

    private final Collection<Listener> listeners = new LinkedList<>();

    @Autowired
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

    public void publishEnter(@NonNull Team team, @NonNull Utente utente) {
        getListenersOfType(TeamJoinListener.class)
                .forEach(listener -> listener.notifyEnterTeam(team, utente));
    }

}
