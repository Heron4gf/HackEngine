package it.unicam.ids2026.core.hackathon;

import it.unicam.ids2026.core.hackathon.data.DatiHackathon;
import it.unicam.ids2026.core.hackathon.data.Intervallo;
import it.unicam.ids2026.core.hackathon.data.Sottomissione;
import it.unicam.ids2026.core.hackathon.status.RappresentazioneStato;
import it.unicam.ids2026.core.hackathon.status.StatoHackathon;
import it.unicam.ids2026.core.hackathon.status.StatoIscrizione;
import it.unicam.ids2026.core.roles.staff.Giudice;
import it.unicam.ids2026.core.roles.staff.Mentore;
import it.unicam.ids2026.core.roles.staff.Organizzatore;
import it.unicam.ids2026.core.roles.team.Iscrizione;
import it.unicam.ids2026.core.roles.team.Team;
import it.unicam.ids2026.core.supportRequest.RichiestaSupporto;
import it.unicam.ids2026.core.violation.Violazione;
import lombok.*;

import java.util.*;

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
    private Team vincitore;
    private final Set<Mentore> mentori;

    private final Map<Team, Iscrizione> iscritti;
    private final Set<Violazione> violazioni;
    @Getter(AccessLevel.PRIVATE)
    private StatoHackathon state;

    public Hackathon(@NonNull Organizzatore organizzatore, @NonNull DatiHackathon datiHackathon, @NonNull Giudice giudice,
                     @NonNull Intervallo periodoIscrizioni, @NonNull Intervallo durataHackathon) {
        this(
                UUID.randomUUID(),
                organizzatore,
                datiHackathon,
                giudice,
                periodoIscrizioni,
                durataHackathon,
                null,
                new HashSet<>(),
                new LinkedHashMap<>(),
                new HashSet<>(),
                new StatoIscrizione()
        );
    }

    public void nextState() {
        this.state.next(this);
    }

    public void iscriviTeam(Team team) {
        this.state.iscriviTeam(this, team);
    }

    public void aggiungiMentore(Mentore mentore) {
        this.state.aggiungiMentore(this, mentore);
    }

    public void aggiungiSottomissione(Team team, Sottomissione sottomissione) {
        this.state.aggiungiSottomissione(this, team, sottomissione);
    }

    public void aggiungiRichiestaSupporto(Team team, RichiestaSupporto richiestaSupporto) {
        this.state.aggiungiRichiestaSupporto(this, team, richiestaSupporto);
    }

    public void assegnaVincitore(Team team) {
        this.state.assegnaVincitore(this, team);
    }

    public RappresentazioneStato getRappresentazioneStato() {
        return this.state.getRappresentazioneStato();
    }
}