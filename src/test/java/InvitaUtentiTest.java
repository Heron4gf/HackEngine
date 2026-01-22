import it.unicam.ids2026.hackhub.managers.InviteManager;
import it.unicam.ids2026.hackhub.roles.team.Invito;
import it.unicam.ids2026.hackhub.roles.team.Team;
import it.unicam.ids2026.hackhub.roles.team.Utente;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.*;

class InvitaUtentiTest {

    private InviteManager inviteManager;
    private Utente mittente;
    private Utente destinatario;
    private Team team;

    @BeforeEach
    void setUp() {
        inviteManager = new InviteManager();
        mittente = new Utente("Capitano");
        destinatario = new Utente("Recluta");
        team = new Team("Alpha", 5, new HashSet<>());

        team.getMembri().add(mittente);
        mittente.setTeam(team);
    }

    @Test
    void testInvioInvito() {
        inviteManager.invitaUtente(team, destinatario);

        assertEquals(1, destinatario.getCasellaInviti().size());
        Invito invito = destinatario.getCasellaInviti().iterator().next();
        assertEquals(team, invito.getMittente());
    }

    @Test
    void testAccettaInvito() {
        inviteManager.invitaUtente(team, destinatario);
        Invito invito = destinatario.getCasellaInviti().iterator().next();

        inviteManager.accettaInvito(invito);

        assertTrue(destinatario.haTeam());
        assertEquals(team, destinatario.getTeam());
        assertTrue(team.getMembri().contains(destinatario));
        assertTrue(destinatario.getCasellaInviti().isEmpty());
    }

    @Test
    void testRifiutaInvito() {
        inviteManager.invitaUtente(team, destinatario);
        Invito invito = destinatario.getCasellaInviti().iterator().next();

        inviteManager.rifiutaInvito(invito);

        assertFalse(destinatario.haTeam());
        assertTrue(destinatario.getCasellaInviti().isEmpty());
    }
}