package it.unicam.ids2026.core.roles.staff;

import it.unicam.ids2026.core.hackathon.Hackathon;
import it.unicam.ids2026.core.roles.User;

import java.util.Set;

public interface MembroStaff extends User {
    String getCognome();
    Set<Hackathon> getAssociatedHackathons();
    void addAssociatedHackathon(Hackathon hackathon);
}
