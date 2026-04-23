package it.unicam.ids2026.core.supportRequest.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class RispostaTestuale implements RispostaRichiesta{
    private final String messaggio;
}
