package it.unicam.ids2026.core.roles.team;

import it.unicam.ids2026.core.transaction.IParteDiPagamento;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.NonNull;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@EqualsAndHashCode
public class Team implements IParteDiPagamento {

    @EqualsAndHashCode.Exclude
    private final UUID id = UUID.randomUUID();

    @NonNull
    @NotBlank
    @Size(min = 3, max = 30, message = "Il nome del team deve avere tra 3 e 30 caratteri")
    private String nome;

    @Min(value = 1, message = "Il team deve avere almeno 1 membro")
    @Max(value = 20, message = "Il team può avere al massimo 20 membri")
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
