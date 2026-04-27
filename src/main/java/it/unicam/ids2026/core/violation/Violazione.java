package it.unicam.ids2026.core.violation;

import it.unicam.ids2026.core.hackathon.Hackathon;
import it.unicam.ids2026.core.roles.staff.Mentore;
import it.unicam.ids2026.core.roles.staff.Organizzatore;
import it.unicam.ids2026.core.roles.team.Team;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
public class Violazione {

    Long id;
    @NonNull
    private Mentore segnalatoDa;
    @NonNull
    private Team colpevole;
    @NonNull
    private Hackathon hackathon;
    @NonNull
    private String motivazione;
    private Organizzatore gestitoDa;
    private StatoViolazione stato = StatoViolazione.SOLLEVATA;
}
