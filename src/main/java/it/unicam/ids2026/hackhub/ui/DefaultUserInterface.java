package it.unicam.ids2026.hackhub.ui;

import it.unicam.ids2026.hackhub.HackHub;
import it.unicam.ids2026.hackhub.hackathon.data.DatiHackathon;
import it.unicam.ids2026.hackhub.hackathon.Hackathon;
import it.unicam.ids2026.hackhub.hackathon.data.Intervallo;
import it.unicam.ids2026.hackhub.roles.staff.Giudice;
import it.unicam.ids2026.hackhub.roles.staff.Mentore;
import it.unicam.ids2026.hackhub.roles.staff.Organizzatore;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.util.Collection;
import java.util.List;

/**
 * Implementazione standard dell'interfaccia utente.
 * Agisce come bridge tra l'input dell'utente e i manager del sistema HackHub.
 */
@RequiredArgsConstructor
public class DefaultUserInterface implements UserInterface {
    private final HackHub hackHub;

    /**
     * {@inheritDoc}
     * Delega la richiesta al {@link it.unicam.ids2026.hackhub.managers.MentorManager}.
     */
    @Override
    public Collection<Mentore> richiestaMentoriDisponibili(@NonNull Hackathon hackathon) {
        return hackHub.getMentorManager().getMentoriDisponibili(hackathon);
    }

    /**
     * {@inheritDoc}
     * Inoltra i dati all'{@link it.unicam.ids2026.hackhub.managers.HackathonManager} per la creazione effettiva.
     */
    @Override
    public Hackathon createHackathon(@NonNull Organizzatore organizzatore, @NonNull DatiHackathon datiHackathon, @NonNull Giudice giudice,
                                     @NonNull Intervallo periodoIscrizioni, @NonNull Intervallo durataHackathon) throws Exception {
        return hackHub.getHackathonManager().creaHackathon(organizzatore, datiHackathon, giudice, periodoIscrizioni, durataHackathon);
    }

    /**
     * {@inheritDoc}
     * Aggiorna lo stato dell'hackathon aggiungendo i mentori tramite l'HackathonManager.
     */
    @Override
    public Collection<Mentore> selezionaMentori(@NonNull Hackathon h, @NonNull List<Mentore> mentori) {
        hackHub.getHackathonManager().aggiungiMentori(h, mentori);
        return mentori;
    }
}