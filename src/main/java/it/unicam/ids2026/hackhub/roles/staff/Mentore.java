package it.unicam.ids2026.hackhub.roles.staff;

import java.util.UUID;

public class Mentore extends AbstractMembroStaff {
    public Mentore(String nome, String cognome, UUID id) {
        super(id, nome, cognome);
    }
}
