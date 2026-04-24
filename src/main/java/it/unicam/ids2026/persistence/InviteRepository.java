package it.unicam.ids2026.persistence;

import it.unicam.ids2026.core.roles.team.Invito;
import it.unicam.ids2026.core.roles.team.Team;
import it.unicam.ids2026.core.roles.team.Utente;
import lombok.NonNull;

import java.util.Set;

public interface InviteRepository {

    void save(@NonNull Invito invito);

    Set<Invito> findByDestinatario(@NonNull Utente destinatario);

    void delete(@NonNull Utente destinatario, @NonNull Invito invito);

    void deleteByMittente(@NonNull Team team);
}
