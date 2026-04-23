package it.unicam.ids2026.core.hackathon.data;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record Valutazione(

        @Min(value = 0)
        @Max(value = 10, message = "Il voto massimo è 10")
        int voto,

        @NotBlank
        @Size(min = 3, max = 200, message = "Il giudizio deve avere tra 3 e 200 caratteri")
        String giudizio

) {}