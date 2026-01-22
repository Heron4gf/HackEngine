package it.unicam.ids2026.hackhub.roles.staff;

import it.unicam.ids2026.hackhub.hackathon.Hackathon;
import it.unicam.ids2026.hackhub.roles.User;

import java.util.Set;

public interface MembroStaff extends User {
    String getCognome();
    Set<Hackathon> getAssociatedHackathons();
    void addAssociatedHackathon(Hackathon hackathon);
}
