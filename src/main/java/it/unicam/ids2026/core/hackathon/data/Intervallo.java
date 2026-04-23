package it.unicam.ids2026.core.hackathon.data;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.FutureOrPresent;
import lombok.NonNull;

import java.time.LocalDateTime;

public record Intervallo(
        @NonNull @FutureOrPresent(message = "La data di inizio deve essere futura") LocalDateTime dataInizio,
        @NonNull @Future(message = "La data di fine dev'essere futura") LocalDateTime dataFine
) {

    public Intervallo {
        if (dataFine.isBefore(dataInizio)) {
            throw new IllegalArgumentException("La data di fine precede quella di inizio");
        }
    }

    public boolean contiene(LocalDateTime data) {
        return data != null && !data.isBefore(dataInizio) && !data.isAfter(dataFine);
    }
}