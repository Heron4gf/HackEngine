package it.unicam.ids2026.hackhub.managers;

import it.unicam.ids2026.hackhub.roles.Mentore;

import java.util.Collection;

public class MentorManager {

    private Collection<Mentore> listaMentori;

    public Collection<Mentore> getMentoriDisponibili() {
        return listaMentori;
    }

}
