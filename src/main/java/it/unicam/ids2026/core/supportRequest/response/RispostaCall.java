package it.unicam.ids2026.core.supportRequest.response;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@RequiredArgsConstructor
public class RispostaCall implements RispostaRichiesta {
    private final String dettagli = "https://link-call/"+ UUID.randomUUID();
    private final LocalDateTime dataCall;
}
