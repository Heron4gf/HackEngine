package it.unicam.ids2026.core.roles.staff;

import it.unicam.ids2026.core.roles.team.AbstractUser;
import lombok.Getter;
import lombok.NonNull;
import org.jetbrains.annotations.NotNull;

@Getter
public abstract class AbstractMembroStaff extends AbstractUser implements MembroStaff {

    @NonNull
    private final String cognome;

    public AbstractMembroStaff(String nome, @NotNull String cognome) {
        super(nome);
        this.cognome = cognome;
    }

}