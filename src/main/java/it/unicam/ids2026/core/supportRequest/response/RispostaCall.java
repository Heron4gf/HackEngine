package it.unicam.ids2026.core.supportRequest.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class RispostaCall implements RispostaRichiesta {
    private final String dettagli;
    private final LocalDateTime dataCall;
}
