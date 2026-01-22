package it.unicam.ids2026.hackhub.roles.staff;

import it.unicam.ids2026.hackhub.hackathon.Hackathon;

import java.util.Set;
import java.util.UUID;

public class Mentore extends AbstractMembroStaff {

    public Mentore(String nome, String cognome) {
        super(nome, cognome);
    }

    public Mentore(UUID id, String nome, String cognome, Set<Hackathon> associatedHackathons) {
        super(id, nome, cognome, associatedHackathons);
    }
}
