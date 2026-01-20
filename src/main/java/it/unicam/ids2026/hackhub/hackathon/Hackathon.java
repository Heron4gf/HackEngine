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
@RequiredArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Hackathon {

    @EqualsAndHashCode.Include
    private final UUID id = UUID.randomUUID();

    @NonNull
    private final Organizzatore organizzatore;

    @NonNull
    private final DatiHackathon datiHackathon;

    @NonNull
    private final Giudice giudice;

    @NonNull
    private final Intervallo periodoIscrizioni;

    @NonNull
    private final Intervallo durataHackathon;

    private final Set<Mentore> mentori = new HashSet<>();

    private final Set<Sottomissione> sottomissioni = new LinkedHashSet<>();

    private final Set<Team> iscritti;

    @Setter(AccessLevel.PACKAGE)
    private StatoHackathon state = new StatoIscrizione();

    public void aggiungiMentore(Mentore mentore) {
        state.aggiungiMentore(this, mentore);
    }

    public void aggiungiSottomissione(Sottomissione sottomissione) {
        state.aggiungiSottomissione(this, sottomissione);
    }

    public void nextState() {
        state.next(this);
    }

    public void doAggiungiMentore(Mentore mentore) {
        this.mentori.add(mentore);
    }

    public void doAggiungiSottomissione(Sottomissione sottomissione) {
        this.sottomissioni.add(sottomissione);
    }
}