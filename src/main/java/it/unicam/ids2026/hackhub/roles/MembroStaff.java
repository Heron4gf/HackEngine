package it.unicam.ids2026.hackhub.roles;

import java.util.UUID;

public interface MembroStaff extends Utente {

    String getNome();
    String getCognome();
    UUID getId();

}
