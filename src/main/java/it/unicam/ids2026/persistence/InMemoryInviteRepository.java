package it.unicam.ids2026.persistence;

import it.unicam.ids2026.core.roles.team.Invito;
import it.unicam.ids2026.core.roles.team.Team;
import it.unicam.ids2026.core.roles.team.Utente;
import lombok.NonNull;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class InMemoryInviteRepository implements InviteRepository {

    private final Map<Utente, Set<Invito>> inviti = new ConcurrentHashMap<>();

    @Override
    public void save(@NonNull Invito invito) {
        inviti.computeIfAbsent(invito.getDestinatario(), ignored -> ConcurrentHashMap.newKeySet())
                .add(invito);
    }

    @Override
    public Set<Invito> findByDestinatario(@NonNull Utente destinatario) {
        return new HashSet<>(inviti.getOrDefault(destinatario, Set.of()));
    }

    @Override
    public void delete(@NonNull Utente destinatario, @NonNull Invito invito) {
        if (inviti.containsKey(destinatario)) {
            inviti.get(destinatario).remove(invito);
        }
    }

    @Override
    public void deleteByMittente(@NonNull Team team) {
        inviti.values().stream()
                .flatMap(Collection::stream)
                .filter(invito -> invito.getMittente().equals(team))
                .toList()
                .forEach(invito -> delete(invito.getDestinatario(), invito));
    }
}
