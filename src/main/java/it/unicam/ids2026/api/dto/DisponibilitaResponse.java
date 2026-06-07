package it.unicam.ids2026.api.dto;

import it.unicam.ids2026.core.hackathon.data.Disponibilita;

import java.util.LinkedHashSet;
import java.util.Set;
import java.util.stream.Collectors;

public record DisponibilitaResponse(Set<IntervalloDTO> disponibilita) {
    public static DisponibilitaResponse from(Disponibilita d) {
        Set<IntervalloDTO> slots = d.getDisponibilita().stream()
                .map(IntervalloDTO::from)
                .collect(Collectors.toCollection(LinkedHashSet::new));
        return new DisponibilitaResponse(slots);
    }
}
