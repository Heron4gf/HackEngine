package it.unicam.ids2026.unit;

import it.unicam.ids2026.core.hackathon.Hackathon;
import it.unicam.ids2026.core.hackathon.data.DatiHackathon;
import it.unicam.ids2026.core.hackathon.data.Intervallo;
import it.unicam.ids2026.core.hackathon.data.Sottomissione;
import it.unicam.ids2026.core.hackathon.data.Valutazione;
import it.unicam.ids2026.core.hackathon.status.StatoInValutazione;
import it.unicam.ids2026.core.managers.HackathonManager;
import it.unicam.ids2026.core.managers.WinningManager;
import it.unicam.ids2026.core.roles.staff.Giudice;
import it.unicam.ids2026.core.roles.staff.Organizzatore;
import it.unicam.ids2026.core.roles.team.Iscrizione;
import it.unicam.ids2026.core.roles.team.Team;
import it.unicam.ids2026.core.transaction.MoneyAmount;
import it.unicam.ids2026.core.transaction.Transaction;
import it.unicam.ids2026.core.transaction.factory.TransactionFactory;
import it.unicam.ids2026.persistence.HackathonRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.File;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Currency;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

/**
 * Test unitari per WinningManager.
 */
@ExtendWith(MockitoExtension.class)
class WinningManagerTest {

    @Mock
    private TransactionFactory transactionFactory;
    @Mock
    private HackathonManager hackathonManager;
    @Mock
    private HackathonRepository hackathonRepository;
    @Mock
    private Transaction transaction;

    private WinningManager winningManager;
    private Hackathon hackathon;
    private Team team1;
    private Team team2;

    @BeforeEach
    void setUp() {
        winningManager = new WinningManager(transactionFactory, hackathonManager, hackathonRepository);

        Organizzatore organizzatore = new Organizzatore("Mario", "Rossi");
        Giudice giudice = new Giudice("Luigi", "Verdi");
        DatiHackathon dati = new DatiHackathon(
                "Hackathon Test",
                "Roma",
                new it.unicam.ids2026.core.transaction.MoneyAmount(new BigDecimal("1000.00"), Currency.getInstance("EUR")),
                5,
                "Regolamento test"
        );
        LocalDateTime now = LocalDateTime.now();
        Intervallo iscrizioni = new Intervallo(now.plusDays(1), now.plusDays(10));
        Intervallo durata = new Intervallo(now.plusDays(15), now.plusDays(17));

        hackathon = new Hackathon(organizzatore, dati, giudice, iscrizioni, durata);

        // Crea i team
        team1 = new Team("Team Alpha", 5);
        team2 = new Team("Team Beta", 5);

        // Imposta lo stato a VALUTAZIONE per permettere l'assegnazione del vincitore
        hackathon.setState(new StatoInValutazione());
    }

    @Test
    void controllaSeValutate_TuttiValutati_RestituisceTrue() {
        // Arrange - Iscrivi team con sottomissioni valutate
        hackathon.getIscritti().put(team1, createIscrizioneValutata(team1, 8));
        hackathon.getIscritti().put(team2, createIscrizioneValutata(team2, 7));

        // Act
        boolean result = winningManager.controllaSeValutate(hackathon);

        // Assert
        assertTrue(result);
    }

    @Test
    void controllaSeValutate_AlcuniNonValutati_RestituisceFalse() {
        // Arrange - Un team con valutazione, uno senza
        hackathon.getIscritti().put(team1, createIscrizioneValutata(team1, 8));
        hackathon.getIscritti().put(team2, createIscrizioneNonValutata(team2));

        // Act
        boolean result = winningManager.controllaSeValutate(hackathon);

        // Assert
        assertFalse(result);
    }

    @Test
    void controllaSeValutate_NessunTeamIscritto_RestituisceTrue() {
        // Act
        boolean result = winningManager.controllaSeValutate(hackathon);

        // Assert
        assertTrue(result);
    }

    @Test
    void ottieniTeamConPunteggioMassimo_UnSoloVincitore_RestituisceTeam() {
        // Arrange
        hackathon.getIscritti().put(team1, createIscrizioneValutata(team1, 9));
        hackathon.getIscritti().put(team2, createIscrizioneValutata(team2, 7));

        // Act
        Map<Team, Iscrizione> result = winningManager.ottieniTeamConPunteggioMassimo(hackathon);

        // Assert
        assertEquals(1, result.size());
        assertTrue(result.containsKey(team1));
        assertEquals(9.0, result.get(team1).getSottomissione().getValutazione().voto());
    }

    @Test
    void ottieniTeamConPunteggioMassimo_Pareggio_RestituisceTutti() {
        // Arrange - Entrambi i team hanno lo stesso punteggio
        hackathon.getIscritti().put(team1, createIscrizioneValutata(team1, 8));
        hackathon.getIscritti().put(team2, createIscrizioneValutata(team2, 8));

        // Act
        Map<Team, Iscrizione> result = winningManager.ottieniTeamConPunteggioMassimo(hackathon);

        // Assert
        assertEquals(2, result.size());
        assertTrue(result.containsKey(team1));
        assertTrue(result.containsKey(team2));
    }

    @Test
    void ottieniTeamConPunteggioMassimo_SottomissioniNonValutate_LanciaEccezione() {
        // Arrange
        hackathon.getIscritti().put(team1, createIscrizioneValutata(team1, 8));
        hackathon.getIscritti().put(team2, createIscrizioneNonValutata(team2));

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
            winningManager.ottieniTeamConPunteggioMassimo(hackathon)
        );
        assertEquals("L'hackathon in questione ha sottomissioni non valutate", exception.getMessage());
    }

    @Test
    void assegnaVincitore_TuttiValutati_AssegnaVincitore() {
        // Arrange
        hackathon.getIscritti().put(team1, createIscrizioneValutata(team1, 9));
        hackathon.getIscritti().put(team2, createIscrizioneValutata(team2, 7));

        // Act
        winningManager.assegnaVincitore(hackathon, team1);

        // Assert
        assertEquals(team1, hackathon.getVincitore());
        verify(hackathonManager).avanzaStato(hackathon);
    }

    @Test
    void elaboraPagamentoPremio_VincitoreValido_SalvaHackathon() {
        // Arrange
        hackathon.setVincitore(team1);
        when(transactionFactory.makePayment(eq(hackathon), eq(team1), any())).thenReturn(transaction);
        when(transaction.getImporto()).thenReturn(new MoneyAmount(new BigDecimal("1000.00"), Currency.getInstance("EUR")));

        // Act
        Transaction result = winningManager.elaboraPagamentoPremio(hackathon, team1);

        // Assert
        assertSame(transaction, result);
        assertTrue(hackathon.getWallet().getTransazioni().contains(transaction));
        verify(hackathonRepository).save(hackathon);
    }

    @Test
    void assegnaVincitore_SottomissioniNonValutate_LanciaEccezione() {
        // Arrange
        hackathon.getIscritti().put(team1, createIscrizioneValutata(team1, 8));
        hackathon.getIscritti().put(team2, createIscrizioneNonValutata(team2));

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
            winningManager.assegnaVincitore(hackathon, team1)
        );
        assertEquals("Ci sono sottomissioni non ancora valutate", exception.getMessage());
    }

    @Test
    void assegnaVincitore_TeamNonIscritto_AssegnaComunque() {
        // Arrange - Solo team1 è iscritto
        hackathon.getIscritti().put(team1, createIscrizioneValutata(team1, 9));

        // Act - Assegna team2 che non è iscritto
        winningManager.assegnaVincitore(hackathon, team2);

        // Assert
        assertEquals(team2, hackathon.getVincitore());
    }

    /**
     * Crea un'iscrizione con valutazione.
     */
    private Iscrizione createIscrizioneValutata(Team team, int voto) {
        Sottomissione sottomissione = new Sottomissione("Sottomissione " + team.getNome(), "Descrizione", new File("."));
        sottomissione.setValutazione(new Valutazione(voto, "Ottimo lavoro"));
        Iscrizione iscrizione = new Iscrizione();
        iscrizione.setSottomissione(sottomissione);
        return iscrizione;
    }

    /**
     * Crea un'iscrizione senza valutazione.
     */
    private Iscrizione createIscrizioneNonValutata(Team team) {
        Sottomissione sottomissione = new Sottomissione("Sottomissione " + team.getNome(), "Descrizione", new File("."));
        Iscrizione iscrizione = new Iscrizione();
        iscrizione.setSottomissione(sottomissione);
        return iscrizione;
    }
}
