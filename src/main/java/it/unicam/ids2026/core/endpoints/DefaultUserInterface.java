package it.unicam.ids2026.core.endpoints;

import it.unicam.ids2026.core.HackHub;
import it.unicam.ids2026.core.hackathon.data.DatiHackathon;
import it.unicam.ids2026.core.hackathon.Hackathon;
import it.unicam.ids2026.core.hackathon.data.Intervallo;
import it.unicam.ids2026.core.managers.StaffManager;
import it.unicam.ids2026.core.roles.staff.Giudice;
import it.unicam.ids2026.core.roles.staff.Mentore;
import it.unicam.ids2026.core.roles.staff.Organizzatore;
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
     * Delega la richiesta al {@link StaffManager}.
     */
    @Override
    public Collection<Mentore> richiestaMentoriDisponibili(@NonNull Hackathon hackathon) {
        return hackHub.getStaffManager().getMentoriDisponibili(hackathon);
    }

    /**
     * {@inheritDoc}
     * Inoltra i dati all'{@link it.unicam.ids2026.core.managers.HackathonManager} per la creazione effettiva.
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