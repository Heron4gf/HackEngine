package it.unicam.ids2026.hackhub.roles.team;

import lombok.Data;

@Data
public class Invito {
    private final Team mittente;
    private final Utente destinatario;

    public Invito(Team mittente, Utente destinatario) {
        this.mittente = mittente;
        this.destinatario = destinatario;
    }

}
