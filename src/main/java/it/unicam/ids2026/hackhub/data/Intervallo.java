package it.unicam.ids2026.hackhub.data;

import java.time.LocalDate;
import java.util.Objects;

public record Intervallo(LocalDate dataInizio, LocalDate dataFine) {

    public Intervallo {
        Objects.requireNonNull(dataInizio);
        Objects.requireNonNull(dataFine);
        if (dataFine.isBefore(dataInizio)) {
            throw new IllegalArgumentException("La data di fine precede quella di inizio");
        }
    }

    public boolean contiene(LocalDate data) {
        return data != null && !data.isBefore(dataInizio) && !data.isAfter(dataFine);
    }
}