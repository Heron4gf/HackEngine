package it.unicam.ids2026.core.hackathon.data;
import lombok.Data;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.io.File;
import java.util.Objects;
@Data
@RequiredArgsConstructor
public class Sottomissione {

   @NonNull
   private String nome;
   @NonNull
   private String descrizione;
   @NonNull
   private File allegato;


}