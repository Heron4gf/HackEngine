package it.unicam.ids2026.hackhub.hackathon;

import it.unicam.ids2026.hackhub.hackathon.data.DatiHackathon;
import it.unicam.ids2026.hackhub.hackathon.data.Intervallo;
import it.unicam.ids2026.hackhub.hackathon.data.Sottomissione;
import it.unicam.ids2026.hackhub.roles.staff.Giudice;
import it.unicam.ids2026.hackhub.roles.staff.Mentore;
import it.unicam.ids2026.hackhub.roles.staff.Organizzatore;
import it.unicam.ids2026.hackhub.roles.team.Team;
import lombok.*;

import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Hackathon {

    @EqualsAndHashCode.Include
    private final UUID id;

    private final Organizzatore organizzatore;
    private final DatiHackathon datiHackathon;
    private final Giudice giudice;
    private final Intervallo periodoIscrizioni;
    private final Intervallo durataHackathon;

    private final Set<Mentore> mentori;
    private final Set<Sottomissione> sottomissioni;
    private final Set<Team> iscritti;

    public StatoHackathon state;

    public Hackathon(@NonNull Organizzatore organizzatore, @NonNull DatiHackathon datiHackathon, @NonNull Giudice giudice,
                     @NonNull Intervallo periodoIscrizioni, @NonNull Intervallo durataHackathon) {
        this(
                UUID.randomUUID(),
                organizzatore,
                datiHackathon,
                giudice,
                periodoIscrizioni,
                durataHackathon,
                new HashSet<>(),
                new LinkedHashSet<>(),
                new HashSet<>(),
                new StatoIscrizione()
        );
    }
}