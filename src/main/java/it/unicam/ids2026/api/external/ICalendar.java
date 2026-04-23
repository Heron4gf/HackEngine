package it.unicam.ids2026.api.external;

import it.unicam.ids2026.core.hackathon.data.Disponibilita;
import it.unicam.ids2026.core.hackathon.data.Intervallo;
import it.unicam.ids2026.core.roles.User;
import lombok.NonNull;

import java.time.LocalDateTime;

public interface ICalendar {

    @NonNull
    Disponibilita getDisponibilita(@NonNull User user, @NonNull Intervallo durataHackathon);

    void fissaImpegno(@NonNull User user, @NonNull LocalDateTime dateTime);
}
