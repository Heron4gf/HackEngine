package it.unicam.ids2026.hackhub.ui;

import it.unicam.ids2026.hackhub.hackathon.data.DatiHackathon;
import it.unicam.ids2026.hackhub.hackathon.Hackathon;
import it.unicam.ids2026.hackhub.hackathon.data.Intervallo;
import it.unicam.ids2026.hackhub.roles.staff.Giudice;
import it.unicam.ids2026.hackhub.roles.staff.Mentore;
import it.unicam.ids2026.hackhub.roles.staff.Organizzatore;
import lombok.NonNull;

import java.util.Collection;
import java.util.List;

/**
 * Interfaccia che definisce le interazioni principali tra l'utente e il sistema HackHub.
 * Gestisce i flussi di input/output per operazioni complesse come la creazione di eventi
 * o la gestione del personale.
 */
public interface UserInterface {

    /**
     * Richiede e restituisce la lista dei mentori disponibili per un dato hackathon.
     * Solitamente filtra i mentori già assegnati all'evento.
     *
     * @param hackathon L'hackathon per il quale si cercano mentori.
     * @return Una collezione di mentori disponibili.
     */
    Collection<Mentore> richiestaMentoriDisponibili(@NonNull Hackathon hackathon);

    /**
     * Gestisce la creazione di un nuovo hackathon raccogliendo e validando i dati necessari.
     *
     * @param organizzatore      L'organizzatore che crea l'evento.
     * @param datiHackathon      I dettagli descrittivi dell'evento.
     * @param giudice            Il giudice principale designato.
     * @param periodoIscrizioni  L'intervallo temporale per le iscrizioni.
     * @param durataHackathon    L'intervallo temporale di svolgimento dell'evento.
     * @return L'istanza dell'Hackathon appena creato.
     * @throws Exception Se si verificano errori durante la validazione o la creazione.
     */
    Hackathon createHackathon(@NonNull Organizzatore organizzatore, @NonNull DatiHackathon datiHackathon, @NonNull Giudice giudice,
                              @NonNull Intervallo periodoIscrizioni, @NonNull Intervallo durataHackathon) throws Exception;

    /**
     * Permette la selezione di un sottoinsieme di mentori da una lista fornita
     * per l'assegnazione a uno specifico hackathon.
     *
     * @param h       L'hackathon a cui assegnare i mentori.
     * @param mentori La lista di candidati mentori tra cui scegliere.
     * @return La collezione dei mentori effettivamente selezionati.
     */
    Collection<Mentore> selezionaMentori(@NonNull Hackathon h, @NonNull List<Mentore> mentori);
}