package it.unicam.ids2026.hackhub.roles.team;


import it.unicam.ids2026.hackhub.roles.User;

public class Invito {
    private final Team mittente;
    private final Utente destinatario;

    public Invito(Team mittente, Utente destinatario) {
        this.mittente = mittente;
        this.destinatario = destinatario;
    }

}
