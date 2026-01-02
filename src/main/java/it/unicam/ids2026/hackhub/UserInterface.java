package it.unicam.ids2026.hackhub;

import it.unicam.ids2026.hackhub.data.DatiHackathon;
import it.unicam.ids2026.hackhub.roles.Mentore;

import java.util.Collection;

public interface UserInterface {
    Collection<Mentore> richiestaMentoriDisponibili();

    //Review return type
    Hackathon createHackathon(DatiHackathon datiHackathon);

    //Review return type
    Collection<Mentore> selezionaMentori(Collection<Mentore> mentori);
}
