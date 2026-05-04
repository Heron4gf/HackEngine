package it.unicam.ids2026.core.hackathon.data;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.io.File;

@Data
@RequiredArgsConstructor
public class Sottomissione {

    @NonNull private String nome;
    @NonNull private String descrizione;
    @NonNull private File allegato;

    private Valutazione valutazione;

    public boolean hasValutazione() {
        return valutazione != null;
    }
}