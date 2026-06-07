package it.unicam.ids2026.api.dto.request;

import it.unicam.ids2026.api.dto.IntervalloDTO;
import it.unicam.ids2026.core.hackathon.data.Disponibilita;
import it.unicam.ids2026.core.hackathon.data.Intervallo;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record UpdateDisponibilitaRequest(@NotNull List<IntervalloDTO> intervalli) {
    public Disponibilita toDisponibilita() {
        Intervallo[] arr = intervalli.stream()
                .map(IntervalloDTO::toIntervallo)
                .toArray(Intervallo[]::new);
        return new Disponibilita(arr);
    }
}
