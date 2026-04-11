package it.unicam.ids2026.core.roles.staff;

import it.unicam.ids2026.core.hackathon.Hackathon;

import java.util.Set;
import java.util.UUID;

public class Giudice extends AbstractMembroStaff {

    public Giudice(String nome, String cognome) {
        super(nome, cognome);
    }

    public Giudice(UUID id, String nome, String cognome, Set<Hackathon> associatedHackathons) {
        super(id, nome, cognome, associatedHackathons);
    }
}
