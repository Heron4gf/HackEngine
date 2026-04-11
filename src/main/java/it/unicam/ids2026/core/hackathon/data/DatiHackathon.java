package it.unicam.ids2026.core.hackathon.data;

import java.math.BigDecimal;
import java.util.Currency;

import lombok.NonNull;

import jakarta.validation.constraints.*;

public record DatiHackathon(

        @NonNull
        @NotBlank(message = "Il nome non può essere vuoto")
        String nome,

        @NonNull
        @NotBlank(message = "Il luogo non può essere vuoto")
        String luogo,

        @NonNull
        @DecimalMin(value = "0.0", inclusive = false, message = "Il premio deve essere maggiore di 0")
        BigDecimal premioInDenaro,

        @NonNull
        Currency currency,

        @Min(value = 1, message = "Il team deve avere almeno 1 partecipante")
        @Max(value = 20, message = "Il team può avere al massimo 20 partecipanti")
        int dimensioneMaxTeam,

        @NonNull
        @NotBlank(message = "Il regolamento non può essere vuoto")
        String regolamento
) {}