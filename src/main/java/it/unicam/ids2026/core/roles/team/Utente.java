package it.unicam.ids2026.core.roles.team;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.UUID;

@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
public class Utente extends AbstractUser {
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private Team team;

    public Utente(String nome) {
        this(UUID.randomUUID(), nome);
    }

    public Utente(UUID id, String nome) {
        this(id, nome, null);
    }

    public Utente(UUID id, String nome, Team team) {
        super(id, nome);
        this.team = team;
    }

    public boolean haTeam() {
        return this.team != null;
    }
}
