package it.unicam.ids2026.api.external;

import it.unicam.ids2026.core.hackathon.data.Disponibilita;
import it.unicam.ids2026.core.hackathon.data.Intervallo;
import it.unicam.ids2026.core.roles.User;
import lombok.NonNull;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class DefaultCalendarWrapper implements ICalendar {
    @Override
    public @NonNull Disponibilita getDisponibilita(@NonNull User user, @NonNull Intervallo durataHackathon) {
        return new Disponibilita(new Intervallo[]{durataHackathon});
    }

    @Override
    public void fissaImpegno(@NonNull User user, @NonNull LocalDateTime dateTime) {

    }
}
