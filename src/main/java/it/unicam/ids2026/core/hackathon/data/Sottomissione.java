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

    @NonNull
    @NotBlank
    @Size(min = 3, max = 30, message = "Il nome deve avere tra 3 e 30 caratteri")
    private String nome;

    @NonNull
    @NotBlank
    @Size(min = 10, max = 200, message = "La descrizione deve avere tra 10 e 200 caratteri")
    private String descrizione;

    @NonNull
    @NotNull(message = "L'allegato non può essere nullo")
    private File allegato;

    private Valutazione valutazione ;
}