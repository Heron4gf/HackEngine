package it.unicam.ids2026.hackhub.ui;

import it.unicam.ids2026.hackhub.hackathon.data.DatiHackathon;
import it.unicam.ids2026.hackhub.hackathon.Hackathon;
import it.unicam.ids2026.hackhub.hackathon.data.Intervallo;
import it.unicam.ids2026.hackhub.roles.staff.Giudice;
import it.unicam.ids2026.hackhub.roles.staff.Mentore;
import it.unicam.ids2026.hackhub.roles.staff.Organizzatore;

import java.util.Collection;
import java.util.List;

public interface UserInterface {
    Collection<Mentore> richiestaMentoriDisponibili(Hackathon hackathon);

    //Review return type
    Hackathon createHackathon(Organizzatore organizzatore, DatiHackathon datiHackathon, Giudice giudice,
                              Intervallo periodoIscrizioni, Intervallo durataHackathon) throws Exception;

    //Review return type
    Collection<Mentore> selezionaMentori(Hackathon h, List<Mentore> mentori);
}
