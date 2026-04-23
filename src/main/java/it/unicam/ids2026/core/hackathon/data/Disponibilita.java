package it.unicam.ids2026.core.hackathon.data;

import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.Set;

@Getter
@RequiredArgsConstructor
public class Disponibilita {

    @NonNull
    private Set<Intervallo> disponibilita;

    // valutare l'utilità di questo
    public Disponibilita(Intervallo[] intervalli) {
        this(new LinkedHashSet<>(Arrays.asList(intervalli))); // mantiene l'ordine di inserimento
    }

    public boolean contains(LocalDateTime dateTime) {
        for(Intervallo intervallo : disponibilita) {
            if(intervallo.contiene(dateTime)) return true;
        }
        return false;
    }
}
