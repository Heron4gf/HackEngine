package it.unicam.ids2026.hackhub.roles.staff;

import it.unicam.ids2026.hackhub.hackathon.Hackathon;

import java.util.Set;
import java.util.UUID;

public class Organizzatore extends AbstractMembroStaff {

    public Organizzatore(String nome, String cognome) {
        super(nome, cognome);
    }

    public Organizzatore(UUID id, String nome, String cognome, Set<Hackathon> associatedHackathons) {
        super(id, nome, cognome, associatedHackathons);
    }
}
