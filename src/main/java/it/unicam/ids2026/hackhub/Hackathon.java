package it.unicam.ids2026.hackhub;

import it.unicam.ids2026.hackhub.data.DatiHackathon;
import it.unicam.ids2026.hackhub.data.Intervallo;
import it.unicam.ids2026.hackhub.data.Sottomissione;
import it.unicam.ids2026.hackhub.data.StatoHackathon;
import it.unicam.ids2026.hackhub.roles.Giudice;
import it.unicam.ids2026.hackhub.roles.Mentore;
import it.unicam.ids2026.hackhub.roles.Organizzatore;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.util.Collection;
import java.util.LinkedList;
import java.util.UUID;

@RequiredArgsConstructor
public class Hackathon {

    @NonNull
    private final Organizzatore organizzatore;

    @NonNull
    @Getter // NON PRESENTE NEL DIAGRAMMA DI FLUSSO
    private final DatiHackathon datiHackathon;

    @NonNull
    @Getter // NON PRESENTE NEL DIAGRAMMA DI FLUSSO
    private final Collection<Mentore> mentori;

    @NonNull
    private final Giudice giudice;

    @Getter
    @NonNull
    private final Intervallo periodoIscrizioni;
    @Getter
    @NonNull
    private final Intervallo durataHackathon;

    private StatoHackathon stato = StatoHackathon.ISCRIZIONE;
    private final Collection<Sottomissione> sottomissioni = new LinkedList<>();

    @Getter
    private final UUID id = UUID.randomUUID();

    public void aggiungiMentore(Mentore mentore) {
        this.mentori.add(mentore);
    }

    public void aggiungiSottomissione(Sottomissione sottomissione) {
        this.sottomissioni.add(sottomissione);
    }

    private StatoHackathon cambiaStato(StatoHackathon nuovoStato) {
        this.stato = nuovoStato;
        return this.stato;
    }

}
