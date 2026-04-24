package it.unicam.ids2026.api.dto.request;

import jakarta.validation.constraints.*;
import jakarta.validation.Valid;
import lombok.NonNull;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Currency;
import java.util.UUID;

public record CreateHackathonRequest(
        @NotNull(message = "L'organizzatore è obbligatorio")
        UUID organizzatoreId,

        @NotBlank(message = "Il nome non può essere vuoto")
        String nome,

        @NotBlank(message = "Il luogo non può essere vuoto")
        String luogo,

        @NotNull(message = "Il premio è obbligatorio")
        @DecimalMin(value = "0.0", inclusive = false, message = "Il premio deve essere maggiore di 0")
        BigDecimal premioInDenaro,

        @NotNull(message = "La valuta è obbligatoria")
        Currency currency,

        @Min(value = 1, message = "Il team deve avere almeno 1 partecipante")
        @Max(value = 20, message = "Il team può avere al massimo 20 partecipanti")
        int dimensioneMaxTeam,

        @NotBlank(message = "Il regolamento non può essere vuoto")
        String regolamento,

        @NotNull(message = "L'intervallo di iscrizione è obbligatorio")
        @Valid
        IntervalloRequest iscrizioni,

        @NotNull(message = "L'intervallo di durata è obbligatorio")
        @Valid
        IntervalloRequest durata,

        @NotNull(message = "Il giudice è obbligatorio")
        UUID giudiceId
) {
    public record IntervalloRequest(
            @NonNull @FutureOrPresent(message = "La data di inizio deve essere futura") LocalDateTime dataInizio,
            @NonNull @Future(message = "La data di fine dev'essere futura") LocalDateTime dataFine
    ) {
        public IntervalloRequest {
            if (dataFine.isBefore(dataInizio)) {
                throw new IllegalArgumentException("La data di fine precede quella di inizio");
            }
        }
    }
}
