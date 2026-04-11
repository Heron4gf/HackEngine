package it.unicam.ids2026.core.roles.team;
import lombok.Data;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class Invito {

    @NonNull
    private final Team mittente;

    @NonNull
    private final Utente destinatario;

}