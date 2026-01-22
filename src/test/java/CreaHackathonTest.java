import it.unicam.ids2026.hackhub.hackathon.Hackathon;
import it.unicam.ids2026.hackhub.hackathon.data.DatiHackathon;
import it.unicam.ids2026.hackhub.hackathon.data.Intervallo;
import it.unicam.ids2026.hackhub.managers.HackathonManager;
import it.unicam.ids2026.hackhub.roles.staff.Giudice;
import it.unicam.ids2026.hackhub.roles.staff.Organizzatore;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Currency;
import java.util.HashSet;
import java.util.Locale;

import static org.junit.jupiter.api.Assertions.*;

class CreaHackathonTest {

    private HackathonManager manager;
    private Organizzatore organizzatore;
    private Giudice giudice;
    private DatiHackathon dati;
    private Intervallo iscrizioni;
    private Intervallo durata;

    @BeforeEach
    void setUp() {
        manager = new HackathonManager(new HashSet<>());
        organizzatore = new Organizzatore("Mario", "Rossi");
        giudice = new Giudice("Luigi", "Verdi");

        dati = new DatiHackathon(
                "Hack2026",
                "Roma",
                new BigDecimal("1000.00"),
                Currency.getInstance(Locale.ITALY),
                5,
                "Regolamento..."
        );

        LocalDateTime now = LocalDateTime.now();
        iscrizioni = new Intervallo(now, now.plusDays(10));
        durata = new Intervallo(now.plusDays(11), now.plusDays(13));
    }

    @Test
    void testCreaHackathon() {
        Hackathon h = manager.creaHackathon(organizzatore, dati, giudice, iscrizioni, durata);

        assertNotNull(h);
        assertNotNull(h.getId());
        assertEquals(organizzatore, h.getOrganizzatore());
        assertEquals(dati, h.getDatiHackathon());
        assertTrue(manager.getHackathons().contains(h));
    }

    @Test
    void testGetHackathon() {
        Hackathon h = manager.creaHackathon(organizzatore, dati, giudice, iscrizioni, durata);
        Hackathon retrieved = manager.getHackathon(h.getId());

        assertEquals(h, retrieved);
    }
}
