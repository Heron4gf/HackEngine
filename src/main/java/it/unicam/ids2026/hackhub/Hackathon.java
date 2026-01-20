package it.unicam.ids2026.hackhub;

import it.unicam.ids2026.hackhub.data.DatiHackathon;
import it.unicam.ids2026.hackhub.data.Intervallo;
import it.unicam.ids2026.hackhub.data.Sottomissione;
import it.unicam.ids2026.hackhub.roles.Giudice;
import it.unicam.ids2026.hackhub.roles.Mentore;
import it.unicam.ids2026.hackhub.roles.Organizzatore;
import it.unicam.ids2026.hackhub.status.StatoHackathon;
import it.unicam.ids2026.hackhub.status.StatoIscrizione;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

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

    @Setter
    private StatoHackathon currentState = new StatoIscrizione();

    public void aggiungiMentore(Mentore mentore) {
        currentState.aggiungiMentore(this, mentore);
    }

    public void aggiungiSottomissione(Sottomissione sottomissione) {
        currentState.aggiungiSottomissione(this, sottomissione);
    }

    public void nextState() {
        currentState.next(this);
    }

    public void doAggiungiMentore(Mentore mentore) {
        this.mentori.add(mentore);
    }

    public void doAggiungiSottomissione(Sottomissione sottomissione) {
        this.sottomissioni.add(sottomissione);
    }
}