package it.unicam.ids2026.hackhub.roles.invito;


import it.unicam.ids2026.hackhub.roles.team.Team;
import it.unicam.ids2026.hackhub.roles.User;

public class Invito {
    private final Team mittente;
    private final User destinatario;

    public Invito(Team mittente, User destinatario) {
        this.mittente = mittente;
        this.destinatario = destinatario;
    }

    public void accetta() {
        mittente.aggiungiUtenti(destinatario);
    }

    public void rifiuta() {
    }
}
