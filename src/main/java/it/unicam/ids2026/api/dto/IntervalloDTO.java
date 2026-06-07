package it.unicam.ids2026.api.dto;

import it.unicam.ids2026.core.hackathon.data.Intervallo;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record IntervalloDTO(
        @NotNull LocalDateTime dataInizio,
        @NotNull LocalDateTime dataFine
) {
    public static IntervalloDTO from(Intervallo intervallo) {
        return new IntervalloDTO(intervallo.dataInizio(), intervallo.dataFine());
    }

    public Intervallo toIntervallo() {
        return new Intervallo(dataInizio, dataFine);
    }
}
