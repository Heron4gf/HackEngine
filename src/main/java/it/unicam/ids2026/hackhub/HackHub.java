package it.unicam.ids2026.hackhub;

import it.unicam.ids2026.hackhub.data.DatiHackathon;
import it.unicam.ids2026.hackhub.roles.Mentore;

import java.util.Collection;

public class HackHub {

    private static HackHub instance;

    private HackHub() {
        // using Singleton
    }

    public static HackHub getInstance() {
        if(instance == null) instance = new HackHub();
        return instance;
    }

    public Collection<Mentore> getMentoriDisponibili() {
        return null;
    }

    public Hackathon getHackathon(int id) {
        return null;
    }

    public void aggiungiMentori(Hackathon hackathon, Collection<Mentore> mentori) {

    }

    public Hackathon creaHackathon(DatiHackathon datiHackathon) {
        return null;
    }

}
