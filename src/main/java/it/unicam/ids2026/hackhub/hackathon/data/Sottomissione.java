package it.unicam.ids2026.hackhub.hackathon.data;
import lombok.AllArgsConstructor;
import lombok.Data;
import java.io.File;

@Data
@AllArgsConstructor
public class Sottomissione {

    private String nome;
    private String descrizione;
    private File allegato;

}