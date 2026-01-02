package it.unicam.ids2026.hackhub;

import it.unicam.ids2026.hackhub.data.DatiHackathon;
import it.unicam.ids2026.hackhub.data.Intervallo;
import it.unicam.ids2026.hackhub.managers.HackathonManager;
import it.unicam.ids2026.hackhub.managers.MentorManager;
import it.unicam.ids2026.hackhub.roles.Giudice;
import it.unicam.ids2026.hackhub.roles.Mentore;
import it.unicam.ids2026.hackhub.roles.Organizzatore;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

@NoArgsConstructor(access = AccessLevel.PRIVATE) // using Singleton
public class HackHub {

    @Getter(lazy = true) // Thread-safe implementation using Lombok
    private static final HackHub instance = new HackHub();

    private final HackathonManager hackathonManager = new HackathonManager();
    private final MentorManager mentorManager = new MentorManager(List.of() /*mock list as placeholder*/);

    /**
     * Retrieve mentors available for the given hackathon.
     *
     * @param hackathon the hackathon to query for available mentors
     * @return a collection of Mentore available for the specified hackathon
     */
    public Collection<Mentore> getMentoriDisponibili(Hackathon hackathon) {
        return mentorManager.getMentoriDisponibili(hackathon);
    }

    /**
     * Retrieve the Hackathon with the given identifier.
     *
     * @param id the UUID of the hackathon to retrieve
     * @return the Hackathon matching the given id, or `null` if no such hackathon exists
     */
    public Hackathon getHackathon(UUID id) {
        return hackathonManager.getHackathon(id);
    }

    /**
     * Adds the given mentors to the specified hackathon.
     *
     * @param hackathon the target hackathon
     * @param mentori   the collection of mentors to add to the hackathon
     */
    public void aggiungiMentori(Hackathon hackathon, Collection<Mentore> mentori) {
        hackathonManager.aggiungiMentori(hackathon, mentori);
    }

    /**
     * Creates a new Hackathon with the given organizer, metadata, judge, registration period, and duration.
     *
     * @param organizzatore        the organizer who creates the hackathon
     * @param datiHackathon        the metadata/details for the hackathon
     * @param giudice              the judge assigned to the hackathon
     * @param periodoIscrizioni    the registration period for the hackathon
     * @param durataHackathon      the time interval representing the hackathon's duration
     * @return                     the created Hackathon
     * @throws Exception           if creation fails (e.g., validation or persistence error)
     */
    public Hackathon creaHackathon(Organizzatore organizzatore, DatiHackathon datiHackathon, Giudice giudice, Intervallo periodoIscrizioni, Intervallo durataHackathon) throws Exception {
        return hackathonManager.creaHackathon(organizzatore, datiHackathon, giudice, periodoIscrizioni, durataHackathon);
    }

}