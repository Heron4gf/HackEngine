package it.unicam.ids2026.hackhub.roles.staff;

import java.util.UUID;

public class Giudice extends AbstractMembroStaff {
    public Giudice(String nome, String cognome, UUID id) {
        super(id, nome, cognome);
    }
}
