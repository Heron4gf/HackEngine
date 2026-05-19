package it.unicam.ids2026.api.dto.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record EvaluationRequest(
        @Min(0)
        @Max(10)
        int voto,

        @NotBlank
        @Size(min = 3, max = 200)
        String giudizio
) {}