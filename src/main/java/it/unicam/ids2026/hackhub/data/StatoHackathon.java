package it.unicam.ids2026.hackhub.data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor // genera il costruttore per i final
public enum StatoHackathon {
    ISCRIZIONE("Fase d'iscrzione"),
    IN_CORSO("Fase di svolgimento"),
    IN_VALUTAZIONE("Fase di valutazione"),
    CONCLUSO("Concluso");

    private final String state;
}
