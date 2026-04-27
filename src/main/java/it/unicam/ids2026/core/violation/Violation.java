package it.unicam.ids2026.core.violation;

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
public class Violation {
    @NonNull
    private Mentore segnalatoDa;
    @NonNull
    private Team colpevole;
    private Organizzatore gestitoDa;
    @NonNull
    private String motivazione;
    private ViolationStatus stato = ViolationStatus.SOLLEVATA;
}
