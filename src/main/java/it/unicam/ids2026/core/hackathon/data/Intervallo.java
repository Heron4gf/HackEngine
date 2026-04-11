package it.unicam.ids2026.core.hackathon.data;

import lombok.NonNull;

import java.time.LocalDateTime;

public record Intervallo(@NonNull LocalDateTime dataInizio, @NonNull LocalDateTime dataFine) {

    public Intervallo {
        if (dataFine.isBefore(dataInizio)) {
            throw new IllegalArgumentException("La data di fine precede quella di inizio");
        }
    }

    public boolean contiene(LocalDateTime data) {
        return data != null && !data.isBefore(dataInizio) && !data.isAfter(dataFine);
    }
}