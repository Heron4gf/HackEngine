package it.unicam.ids2026.api.events;

import it.unicam.ids2026.core.hackathon.Hackathon;
import lombok.NonNull;

@FunctionalInterface
public interface HackathonChangeListener extends Listener {

    void notifyHackathonChange(@NonNull Hackathon hackathon);
}
