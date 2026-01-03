package it.unicam.ids2026.hackhub;

import it.unicam.ids2026.hackhub.data.DatiHackathon;
import it.unicam.ids2026.hackhub.data.Intervallo;
import it.unicam.ids2026.hackhub.roles.Giudice;
import it.unicam.ids2026.hackhub.roles.Mentore;
import it.unicam.ids2026.hackhub.roles.Organizzatore;

import java.util.Collection;
public class DefaultUserInterface implements UserInterface {
    private HackHub hackHub;
    public DefaultUserInterface(HackHub hackHub) {
        this.hackHub = hackHub;
    }

    @Override
    public Collection<Mentore> richiestaMentoriDisponibili(Hackathon hackathon) {
        return hackHub.getMentoriDisponibili(hackathon);
    }

    @Override
    public Hackathon createHackathon(Organizzatore organizzatore, DatiHackathon datiHackathon, Giudice giudice,
                                     Intervallo periodoIscrizioni, Intervallo durataHackathon) throws Exception {
        return hackHub.creaHackathon(organizzatore, datiHackathon, giudice, periodoIscrizioni, durataHackathon);
    }

    @Override
    public Collection<Mentore> selezionaMentori(Hackathon h, Collection<Mentore> mentori) {
        hackHub.aggiungiMentori(h, mentori);
        return mentori;
    }


}
