package it.unicam.ids2026.hackhub.roles.staff;

import it.unicam.ids2026.hackhub.hackathon.Hackathon;
import it.unicam.ids2026.hackhub.roles.team.AbstractUser;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;
import java.util.UUID;

@Getter
public abstract class AbstractMembroStaff extends AbstractUser implements MembroStaff {

    private final String cognome;

    @Setter
    private Set<Hackathon> associatedHackathons;

    public AbstractMembroStaff(UUID id, String nome, String cognome) {
        super(id, nome);
        this.cognome = cognome;
    }
}