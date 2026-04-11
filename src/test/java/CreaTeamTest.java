import it.unicam.ids2026.core.managers.TeamManager;
import it.unicam.ids2026.core.roles.team.Utente;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.*;

class CreaTeamTest {

    private TeamManager manager;
    private Utente utente;

    @BeforeEach
    void setUp() {
        manager = new TeamManager(new HashSet<>());
        utente = new Utente("UtenteTest");
    }

    @Test
    void testCreaTeam() {
        manager.creaTeam(utente, "DevTeam", 4);

        assertTrue(utente.haTeam());
        assertEquals("DevTeam", utente.getTeam().getNome());
        assertNotNull(manager.getTeam("DevTeam"));
    }

    @Test
    void testCreaTeamFallimentoUtenteOccupato() {
        manager.creaTeam(utente, "TeamA", 4);

        assertThrows(IllegalArgumentException.class, () ->
                manager.creaTeam(utente, "TeamB", 4)
        );
    }

    @Test
    void testCreaTeamFallimentoNomeEsistente() {
        manager.creaTeam(utente, "TeamUnique", 4);
        Utente utente2 = new Utente("Utente2");

        assertThrows(IllegalArgumentException.class, () ->
                manager.creaTeam(utente2, "TeamUnique", 4)
        );
    }

    @Test
    void testEsciDalTeam() {
        manager.creaTeam(utente, "ExitTeam", 3);
        manager.esciDalTeam(utente);

        assertFalse(utente.haTeam());
        assertNull(manager.getTeam("ExitTeam"));
    }
}