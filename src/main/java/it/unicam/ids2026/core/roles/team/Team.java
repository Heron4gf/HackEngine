package it.unicam.ids2026.core.roles.team;

import it.unicam.ids2026.core.transaction.IParteDiPagamento;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.NonNull;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@EqualsAndHashCode
public class Team implements IParteDiPagamento {

    @NonNull
    private String nome;

    private int maxMembri;

    @EqualsAndHashCode.Exclude
    private final Set<Utente> membri;

    public Team(String nome, int maxMembri) {
        this(nome, maxMembri, new HashSet<>());
    }

    @Override
    public String dettagliConto() {
        return this.getNome();
    }
}
