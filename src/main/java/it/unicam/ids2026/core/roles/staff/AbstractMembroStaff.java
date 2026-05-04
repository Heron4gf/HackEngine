package it.unicam.ids2026.core.roles.staff;

import it.unicam.ids2026.core.roles.team.AbstractUser;
import lombok.Getter;

@Getter
public abstract class AbstractMembroStaff extends AbstractUser implements MembroStaff {
    private final String cognome;

    public AbstractMembroStaff(String nome, String cognome) {
        super(nome);
        this.cognome = cognome;
    }

}