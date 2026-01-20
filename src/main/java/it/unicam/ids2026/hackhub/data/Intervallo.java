package it.unicam.ids2026.hackhub.data;

import lombok.NonNull;

import java.time.LocalDate;

public record Intervallo(@NonNull LocalDate dataInizio, @NonNull LocalDate dataFine) {

    public Intervallo {
        if (dataFine.isBefore(dataInizio)) {
            throw new IllegalArgumentException("La data di fine precede quella di inizio");
        }
    }

    public boolean contiene(LocalDate data) {
        return data != null && !data.isBefore(dataInizio) && !data.isAfter(dataFine);
    }
}