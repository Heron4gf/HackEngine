import it.unicam.ids2026.hackhub.HackHub;
import it.unicam.ids2026.hackhub.hackathon.Hackathon;
import it.unicam.ids2026.hackhub.hackathon.data.DatiHackathon;
import it.unicam.ids2026.hackhub.hackathon.data.Intervallo;
import it.unicam.ids2026.hackhub.hackathon.data.Sottomissione;
import it.unicam.ids2026.hackhub.roles.*;
import it.unicam.ids2026.hackhub.hackathon.StatoInCorso;
import it.unicam.ids2026.hackhub.hackathon.StatoIscrizione;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class IscriviTeamTest {

    private HackHub hackHub;
    private Organizzatore organizzatore;
    private Giudice giudice;
    private DatiHackathon dati;
    private Intervallo iscrizioni;
    private Intervallo durata;

    @BeforeEach
    void setup() {
        // Recuperiamo l'istanza singleton
        hackHub = HackHub.getInstance();

        // Dati comuni per i test
        organizzatore = new Organizzatore("Mario", "Rossi", UUID.randomUUID());
        giudice = new Giudice("Luigi", "Verdi", UUID.randomUUID());
        dati = new DatiHackathon("HackTest Flow", "Online", 4, "regole finte");
        iscrizioni = new Intervallo(LocalDate.now(), LocalDate.now().plusDays(5));
        durata = new Intervallo(LocalDate.now().plusDays(6), LocalDate.now().plusDays(8));
    }

    @Test
    void testFlussoIscrizioneEStati() throws Exception {
        // 1. Creazione Hackathon tramite HackHub
        Hackathon hackathon = hackHub.creaHackathon(organizzatore, dati, giudice, iscrizioni, durata);

        // Verifica stato iniziale
        assertNotNull(hackathon);
        assertTrue(hackathon.getState() instanceof StatoIscrizione, "Lo stato iniziale deve essere ISCRIZIONE");

        Team teamAlpha = new Team(4, "TeamAlpha");

        // 3. Iscrizione del Team (Stato: ISCRIZIONE -> Permesso)
        assertDoesNotThrow(() -> hackathon.getState().iscriviTeam(hackathon, teamAlpha));

        // Verifica che il team sia presente (assumendo tu abbia aggiunto il getter getTeams() in Hackathon)
        assertTrue(hackathon.getIscritti().contains(teamAlpha));

        // 4. Tentativo di Sottomissione (Stato: ISCRIZIONE -> Vietato)
        Sottomissione sottomissione = new Sottomissione();
        assertThrows(IllegalStateException.class, () -> hackathon.aggiungiSottomissione(sottomissione));

        // 5. Cambio di Stato (ISCRIZIONE -> IN_CORSO)
        hackathon.nextState();
        assertTrue(hackathon.getState() instanceof StatoInCorso, "Lo stato deve essere passato a IN_CORSO");

        // 6. Tentativo di Iscrizione Tardiva (Stato: IN_CORSO -> Vietato)
        Team teamLate = new Team(4, "TeamLate");
        assertThrows(RuntimeException.class,
                () -> hackathon.getState().iscriviTeam(hackathon, teamLate),
                "Non deve essere possibile iscriversi ad hackathon iniziati");

        // 7. Tentativo di Sottomissione (Stato: IN_CORSO -> Permesso)
        assertDoesNotThrow(() -> hackathon.aggiungiSottomissione(sottomissione));
        assertTrue(hackathon.getSottomissioni().contains(sottomissione), "La sottomissione dovrebbe essere registrata");
    }
}