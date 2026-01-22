package it.unicam.ids2026.hackhub.ui;

import it.unicam.ids2026.hackhub.HackHub;
import it.unicam.ids2026.hackhub.hackathon.data.DatiHackathon;
import it.unicam.ids2026.hackhub.hackathon.Hackathon;
import it.unicam.ids2026.hackhub.hackathon.data.Intervallo;
import it.unicam.ids2026.hackhub.roles.staff.Giudice;
import it.unicam.ids2026.hackhub.roles.staff.Mentore;
import it.unicam.ids2026.hackhub.roles.staff.Organizzatore;

import java.util.Collection;
import java.util.List;

public class DefaultUserInterface implements UserInterface {
    private HackHub hackHub;
    public DefaultUserInterface(HackHub hackHub) {
        this.hackHub = hackHub;
    }

    @Override
    public Collection<Mentore> richiestaMentoriDisponibili(Hackathon hackathon) {
        return hackHub.getMentorManager().getMentoriDisponibili(hackathon);
    }

    @Override
    public Hackathon createHackathon(Organizzatore organizzatore, DatiHackathon datiHackathon, Giudice giudice,
                                     Intervallo periodoIscrizioni, Intervallo durataHackathon) throws Exception {
        return hackHub.getHackathonManager().creaHackathon(organizzatore, datiHackathon, giudice, periodoIscrizioni, durataHackathon);
    }

    @Override
    public Collection<Mentore> selezionaMentori(Hackathon h, List<Mentore> mentori) {
        hackHub.getHackathonManager().aggiungiMentori(h, mentori);
        return mentori;
    }


}
