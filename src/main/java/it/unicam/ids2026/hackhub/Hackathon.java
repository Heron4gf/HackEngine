package it.unicam.ids2026.hackhub;

import it.unicam.ids2026.hackhub.data.DatiHackathon;
import it.unicam.ids2026.hackhub.data.Intervallo;
import it.unicam.ids2026.hackhub.data.Sottomissione;
import it.unicam.ids2026.hackhub.roles.Mentore;
import it.unicam.ids2026.hackhub.roles.Organizzatore;
import it.unicam.ids2026.hackhub.status.StatoHackathon;
import it.unicam.ids2026.hackhub.status.StatoIscrizione;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.util.LinkedHashSet;
import java.util.Set;
import java.util.UUID;

@RequiredArgsConstructor
public class Hackathon {

    @NonNull
    private final Organizzatore organizzatore;

    @NonNull
    @Getter
    private final DatiHackathon datiHackathon;

    @NonNull
    @Getter
    private final Set<Mentore> mentori;

    @Getter
    @NonNull
    private final Intervallo periodoIscrizioni;

    @Getter
    @NonNull
    private final Intervallo durataHackathon;

    @Getter
    private final Set<Sottomissione> sottomissioni = new LinkedHashSet<>();

    @Getter
    private final UUID id = UUID.randomUUID();

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

    public void setState(StatoHackathon nuovoStato) {
        this.currentState = nuovoStato;
    }

    public void doAggiungiMentore(Mentore mentore) {
        this.mentori.add(mentore);
    }

    public void doAggiungiSottomissione(Sottomissione sottomissione) {
        this.sottomissioni.add(sottomissione);
    }

}