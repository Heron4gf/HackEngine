package it.unicam.ids2026.core.events;

import it.unicam.ids2026.api.events.*;
import it.unicam.ids2026.core.hackathon.Hackathon;
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
    public void publishTeamDeletion(@NonNull Team team) {
        getListenersOfType(TeamDeletionListener.class)
                .forEach(listener -> listener.notifyTeamDeletion(team));
    }

    public void publishUserChange(@NonNull Utente utente) {
        getListenersOfType(UserChangeListener.class)
                .forEach(listener -> listener.notifyUserChange(utente));
    }

    public void publishHackathonChange(@NonNull Hackathon hackathon) {
        getListenersOfType(HackathonChangeListener.class)
                .forEach(listener -> listener.notifyHackathonChange(hackathon));
    }

    public void publishTeamChange(@NonNull Team team) {
        getListenersOfType(TeamChangeListener.class)
                .forEach(listener -> listener.notifyTeamChange(team));
    }

}
