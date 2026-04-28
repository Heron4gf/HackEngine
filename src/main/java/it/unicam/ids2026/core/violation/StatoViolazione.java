package it.unicam.ids2026.core.violation;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum StatoViolazione {
    SOLLEVATA ("Sollevata"),
    PRESA_IN_CARICO ("Presa in carico"),
    CHIUSA ("Chiusa");

    private final String name;
}
