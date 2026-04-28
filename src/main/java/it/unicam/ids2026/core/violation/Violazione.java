package it.unicam.ids2026.core.violation;

import it.unicam.ids2026.core.hackathon.Hackathon;
import it.unicam.ids2026.core.roles.staff.Mentore;
import it.unicam.ids2026.core.roles.staff.Organizzatore;
import it.unicam.ids2026.core.roles.team.Team;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import java.util.UUID;

@Getter
@RequiredArgsConstructor
public class Violazione {

    private final UUID id = UUID.randomUUID();
    @NonNull private final Mentore segnalatoDa;
    @NonNull private final Team colpevole;
    @NonNull private final Hackathon hackathon;
    @NonNull private final String motivazione;

    @Setter private Organizzatore gestitoDa;
    @Setter private StatoViolazione stato = StatoViolazione.SOLLEVATA;
}
