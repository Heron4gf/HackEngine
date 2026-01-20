package it.unicam.ids2026.hackhub.roles.team;

import java.util.UUID;

public class Utente extends AbstractUser {

    public Utente(UUID id, String nome) {
        super(id, nome);
    }
}
