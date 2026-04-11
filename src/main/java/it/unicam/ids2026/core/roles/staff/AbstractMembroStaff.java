package it.unicam.ids2026.core.roles.staff;

import it.unicam.ids2026.core.hackathon.Hackathon;
import it.unicam.ids2026.core.roles.team.AbstractUser;
import lombok.Getter;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Getter
public abstract class AbstractMembroStaff extends AbstractUser implements MembroStaff {

    private final String cognome;

    private final Set<Hackathon> associatedHackathons;

    public AbstractMembroStaff(String nome, String cognome) {
        this(UUID.randomUUID(), nome, cognome);
    }

    public AbstractMembroStaff(UUID id, String nome, String cognome) {
        this(id, nome, cognome, new HashSet<>());
    }

    //Costruttore che fornisce un implementazione di default del set senza richiedere injection
    public AbstractMembroStaff(UUID id, String nome, String cognome, Set<Hackathon> associatedHackathons) {
        super(id, nome);
        this.cognome = cognome;
        this.associatedHackathons = associatedHackathons;
    }

    @Override
    public void addAssociatedHackathon(Hackathon hackathon) {
        this.associatedHackathons.add(hackathon);
    }
}