package it.unicam.ids2026.hackhub.roles.staff;

import it.unicam.ids2026.hackhub.hackathon.Hackathon;
import it.unicam.ids2026.hackhub.roles.User;

public interface MembroStaff extends User {
    String getCognome();
    Hackathon getAssociatedHackathon();
    void setAssociatedHackathon(Hackathon hackathon);
}
