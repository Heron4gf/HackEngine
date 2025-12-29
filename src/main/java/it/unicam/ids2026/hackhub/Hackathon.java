package it.unicam.ids2026.hackhub;

import it.unicam.ids2026.hackhub.data.Intervallo;
import it.unicam.ids2026.hackhub.data.Sottomissione;
import it.unicam.ids2026.hackhub.data.StatoHackathon;
import it.unicam.ids2026.hackhub.roles.Mentore;
import lombok.Getter;

import java.util.UUID;

public class Hackathon {

    private StatoHackathon stato;
    @Getter
    private UUID id;
    @Getter
    private Intervallo periodoIscrizioni;
    @Getter
    private Intervallo durataHackathon;

    public void aggiungiMentore(Mentore mentore) {

    }

    public void aggiungiSottomissione(Sottomissione sottomissione) {

    }

    private StatoHackathon cambiaStato(StatoHackathon nuovoStato) {
        this.stato = nuovoStato;
        return this.stato;
    }

}
