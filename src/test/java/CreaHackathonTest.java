import it.unicam.ids2026.hackhub.HackHub;
import it.unicam.ids2026.hackhub.hackathon.Hackathon;
import it.unicam.ids2026.hackhub.hackathon.data.DatiHackathon;
import it.unicam.ids2026.hackhub.hackathon.data.Intervallo;
import it.unicam.ids2026.hackhub.roles.Giudice;
import it.unicam.ids2026.hackhub.roles.Organizzatore;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class CreaHackathonTest {

    @BeforeAll
    static void setup() {

    }

    @Test
    void testCreaHackathonSuccesso() throws Exception {
        HackHub hackHub = HackHub.getInstance();

        Organizzatore organizzatore = new Organizzatore("Mario", "Rossi", UUID.randomUUID());
        Giudice giudice = new Giudice("Luigi", "Verdi", UUID.randomUUID());
        DatiHackathon dati = new DatiHackathon("HackTest", "Online", 4, "Regolamento");

        Intervallo iscrizioni = new Intervallo(LocalDate.now(), LocalDate.now().plusDays(5));
        Intervallo durata = new Intervallo(LocalDate.now().plusDays(6), LocalDate.now().plusDays(8));

        Hackathon hackathon = hackHub.creaHackathon(organizzatore, dati, giudice, iscrizioni, durata);

        assertNotNull(hackathon);
        assertNotNull(hackathon.getId());
        assertEquals("HackTest", hackathon.getDatiHackathon().getNome());
        assertEquals(iscrizioni, hackathon.getPeriodoIscrizioni());
    }

    @Test
    void testIntervalloInvalido() {
        assertThrows(IllegalArgumentException.class, () ->
                new Intervallo(LocalDate.now().plusDays(5), LocalDate.now())
        );
    }
}