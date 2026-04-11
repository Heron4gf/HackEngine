import it.unicam.ids2026.core.hackathon.Hackathon;
import it.unicam.ids2026.core.hackathon.status.StatoInCorso;
import it.unicam.ids2026.core.hackathon.data.DatiHackathon;
import it.unicam.ids2026.core.hackathon.data.Intervallo;
import it.unicam.ids2026.core.hackathon.data.Sottomissione;
import it.unicam.ids2026.core.managers.HackathonManager;
import it.unicam.ids2026.core.roles.staff.Giudice;
import it.unicam.ids2026.core.roles.staff.Mentore;
import it.unicam.ids2026.core.roles.staff.Organizzatore;
import it.unicam.ids2026.core.roles.team.Team;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Currency;
import java.util.HashSet;
import java.util.Locale;

import static org.junit.jupiter.api.Assertions.*;

class SvolgiHackathonTest {

    private HackathonManager manager;
    private Hackathon hackathon;
    private Mentore mentore;
    private Team team;

    @BeforeEach
    void setUp() {
        manager = new HackathonManager(new HashSet<>());

        Organizzatore org = new Organizzatore("Org", "Staff");
        Giudice giudice = new Giudice("Judge", "Dredd");
        DatiHackathon dati = new DatiHackathon("TestHack", "Online", BigDecimal.ZERO, Currency.getInstance(Locale.US), 4, "Regs");
        LocalDateTime now = LocalDateTime.now();
        Intervallo date = new Intervallo(now, now.plusDays(2));

        hackathon = manager.creaHackathon(org, dati, giudice, date, date);
        mentore = new Mentore("Mentore", "Esperto");
        team = new Team("TeamPartecipante", 3, new HashSet<>());
    }

    @Test
    void testAggiungiMentore() {
        manager.aggiungiMentore(hackathon, mentore);

        assertTrue(hackathon.getMentori().contains(mentore));
    }

    @Test
    void testIscrizioneTeam() {
        manager.iscriviTeam(hackathon, team);

        assertTrue(hackathon.getIscritti().contains(team));
    }

    @Test
    void testAvanzamentoStatoConIscritti() {
        manager.iscriviTeam(hackathon, team);

        assertDoesNotThrow(() -> manager.avanzaStato(hackathon));
        assertTrue(hackathon.getState() instanceof StatoInCorso);
    }

    @Test
    void testAggiungiSottomissione() {
        manager.avanzaStato(hackathon);
        assertTrue(hackathon.getState() instanceof StatoInCorso);
        Sottomissione sub = new Sottomissione();

        // Simulo avanzamento stato se necessario o assumo che lo stato iniziale permetta (o testo metodo pass-through)
        // Per test puro del metodo manager:
        manager.aggiungiSottomissione(hackathon, sub);

        assertTrue(hackathon.getSottomissioni().contains(sub));
    }
}
