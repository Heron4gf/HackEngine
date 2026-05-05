package it.unicam.ids2026.unit;

import it.unicam.ids2026.core.hackathon.Hackathon;
import it.unicam.ids2026.core.hackathon.data.DatiHackathon;
import it.unicam.ids2026.core.hackathon.data.Intervallo;
import it.unicam.ids2026.core.hackathon.status.RappresentazioneStato;
import it.unicam.ids2026.core.managers.HackathonManager;
import it.unicam.ids2026.core.roles.staff.Giudice;
import it.unicam.ids2026.core.roles.staff.Organizzatore;
import it.unicam.ids2026.core.roles.team.Team;
import it.unicam.ids2026.core.roles.team.Utente;
import it.unicam.ids2026.core.transaction.MoneyAmount;
import it.unicam.ids2026.persistence.HackathonRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Currency;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class HackathonManagerTest {

    @Mock
    private HackathonRepository hackathonRepository;

    private HackathonManager hackathonManager;

    @BeforeEach
    void setUp() {
        hackathonManager = new HackathonManager(hackathonRepository);
    }

    @Test
    void creaHackathon_HappyPath_ShouldSaveAndReturnHackathon() {
        // Arrange
        Organizzatore organizzatore = new Organizzatore("Mario", "Rossi");
        Giudice giudice = new Giudice("Luigi", "Verdi");
        DatiHackathon dati = new DatiHackathon(
                "Hackathon Test",
                "Roma",
                new MoneyAmount(new BigDecimal("1000.00"), Currency.getInstance("EUR")),
                5,
                "Regolamento test"
        );
        LocalDateTime now = LocalDateTime.now();
        Intervallo iscrizioni = new Intervallo(now.plusDays(1), now.plusDays(10));
        Intervallo durata = new Intervallo(now.plusDays(15), now.plusDays(17));

        when(hackathonRepository.existsById(any(UUID.class))).thenReturn(false);
        when(hackathonRepository.save(any(Hackathon.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        Hackathon result = hackathonManager.creaHackathon(organizzatore, dati, giudice, iscrizioni, durata);

        // Assert
        assertNotNull(result);
        assertNotNull(result.getId());
        assertEquals("Hackathon Test", result.getDatiHackathon().nome());
        verify(hackathonRepository).save(any(Hackathon.class));
    }

    @Test
    void creaHackathon_IscrizioniEndAfterDurataStart_ShouldThrowIllegalArgumentException() {
        // Arrange
        Organizzatore organizzatore = new Organizzatore("Mario", "Rossi");
        Giudice giudice = new Giudice("Luigi", "Verdi");
        DatiHackathon dati = new DatiHackathon(
                "Hackathon Test",
                "Roma",
                new MoneyAmount(new BigDecimal("1000.00"), Currency.getInstance("EUR")),
                5,
                "Regolamento test"
        );
        LocalDateTime now = LocalDateTime.now();
        // iscrizioni ends AFTER durata starts - invalid
        Intervallo iscrizioni = new Intervallo(now.plusDays(1), now.plusDays(20));
        Intervallo durata = new Intervallo(now.plusDays(15), now.plusDays(17));

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
            hackathonManager.creaHackathon(organizzatore, dati, giudice, iscrizioni, durata)
        );
        assertEquals("Range date inizio o durata invalide", exception.getMessage());
    }

    @Test
    void creaHackathon_IscrizioniStartAfterDurataStart_ShouldThrowIllegalArgumentException() {
        // Arrange
        Organizzatore organizzatore = new Organizzatore("Mario", "Rossi");
        Giudice giudice = new Giudice("Luigi", "Verdi");
        DatiHackathon dati = new DatiHackathon(
                "Hackathon Test",
                "Roma",
                new MoneyAmount(new BigDecimal("1000.00"), Currency.getInstance("EUR")),
                5,
                "Regolamento test"
        );
        LocalDateTime now = LocalDateTime.now();
        // iscrizioni starts AFTER durata starts - invalid
        Intervallo iscrizioni = new Intervallo(now.plusDays(20), now.plusDays(25));
        Intervallo durata = new Intervallo(now.plusDays(15), now.plusDays(17));

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
            hackathonManager.creaHackathon(organizzatore, dati, giudice, iscrizioni, durata)
        );
        assertEquals("Range date inizio o durata invalide", exception.getMessage());
    }

    @Test
    void iscriviTeam_UtenteHasNoTeam_ShouldThrowIllegalArgumentException() {
        // Arrange
        Organizzatore organizzatore = new Organizzatore("Mario", "Rossi");
        Giudice giudice = new Giudice("Luigi", "Verdi");
        DatiHackathon dati = new DatiHackathon(
                "Hackathon Test",
                "Roma",
                new MoneyAmount(new BigDecimal("1000.00"), Currency.getInstance("EUR")),
                5,
                "Regolamento test"
        );
        LocalDateTime now = LocalDateTime.now();
        Intervallo iscrizioni = new Intervallo(now.plusDays(1), now.plusDays(10));
        Intervallo durata = new Intervallo(now.plusDays(15), now.plusDays(17));

        Hackathon hackathon = new Hackathon(organizzatore, dati, giudice, iscrizioni, durata);
        Utente utenteSenzaTeam = new Utente("Utente Test");

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
            hackathonManager.iscriviTeam(hackathon, utenteSenzaTeam)
        );
        assertEquals("Per iscriversi ad un Hackathon l'Utente deve avere un team", exception.getMessage());
    }

    @Test
    void iscriviTeam_TeamAlreadyRegistered_ShouldThrowIllegalArgumentException() {
        // Arrange
        Organizzatore organizzatore = new Organizzatore("Mario", "Rossi");
        Giudice giudice = new Giudice("Luigi", "Verdi");
        DatiHackathon dati = new DatiHackathon(
                "Hackathon Test",
                "Roma",
                new MoneyAmount(new BigDecimal("1000.00"), Currency.getInstance("EUR")),
                5,
                "Regolamento test"
        );
        LocalDateTime now = LocalDateTime.now();
        Intervallo iscrizioni = new Intervallo(now.plusDays(1), now.plusDays(10));
        Intervallo durata = new Intervallo(now.plusDays(15), now.plusDays(17));

        Hackathon hackathon = new Hackathon(organizzatore, dati, giudice, iscrizioni, durata);
        Team team = new Team("Team Test", 5);
        Utente utente = new Utente(UUID.randomUUID(), "Utente Test", team);
        team.getMembri().add(utente);

        // Register team first time
        hackathon.iscriviTeam(team);

        // Act & Assert - Try to register again
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
            hackathonManager.iscriviTeam(hackathon, utente)
        );
        assertEquals("Team gia iscritto", exception.getMessage());
    }

    @Test
    void iscriviTeam_TeamSizeExceedsMax_ShouldThrowIllegalArgumentException() {
        // Arrange
        Organizzatore organizzatore = new Organizzatore("Mario", "Rossi");
        Giudice giudice = new Giudice("Luigi", "Verdi");
        // dimensioneMaxTeam = 2
        DatiHackathon dati = new DatiHackathon(
                "Hackathon Test",
                "Roma",
                new MoneyAmount(new BigDecimal("1000.00"), Currency.getInstance("EUR")),
                2,
                "Regolamento test"
        );
        LocalDateTime now = LocalDateTime.now();
        Intervallo iscrizioni = new Intervallo(now.plusDays(1), now.plusDays(10));
        Intervallo durata = new Intervallo(now.plusDays(15), now.plusDays(17));

        Hackathon hackathon = new Hackathon(organizzatore, dati, giudice, iscrizioni, durata);
        Team team = new Team("Team Test", 5);
        // Add 3 members (exceeds dimensioneMaxTeam of 2)
        Utente utente1 = new Utente(UUID.randomUUID(), "Utente 1", team);
        Utente utente2 = new Utente(UUID.randomUUID(), "Utente 2", team);
        Utente utente3 = new Utente(UUID.randomUUID(), "Utente 3", team);
        team.getMembri().add(utente1);
        team.getMembri().add(utente2);
        team.getMembri().add(utente3);

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
            hackathonManager.iscriviTeam(hackathon, utente1)
        );
        assertEquals("Team troppo grande", exception.getMessage());
    }

    @Test
    void chiudiSottomissioni_HackathonNotInCorso_ShouldThrowIllegalStateException() {
        // Arrange
        Organizzatore organizzatore = new Organizzatore("Mario", "Rossi");
        Giudice giudice = new Giudice("Luigi", "Verdi");
        DatiHackathon dati = new DatiHackathon(
                "Hackathon Test",
                "Roma",
                new MoneyAmount(new BigDecimal("1000.00"), Currency.getInstance("EUR")),
                5,
                "Regolamento test"
        );
        LocalDateTime now = LocalDateTime.now();
        Intervallo iscrizioni = new Intervallo(now.plusDays(1), now.plusDays(10));
        Intervallo durata = new Intervallo(now.plusDays(15), now.plusDays(17));

        Hackathon hackathon = new Hackathon(organizzatore, dati, giudice, iscrizioni, durata);
        // Act & Assert - Hackathon is in ISCRIZIONE state, not IN_CORSO
        IllegalStateException exception = assertThrows(IllegalStateException.class, () ->
            hackathonManager.chiudiSottomissioni(hackathon)
        );
        assertTrue(exception.getMessage().contains("Impossibile chiudere le sottomissioni"));
        assertTrue(exception.getMessage().contains(RappresentazioneStato.ISCRIZIONE.toString()));
    }

    @Test
    void avanzaStato_ShouldCallNextStateAndSave() {
        // Arrange
        Organizzatore organizzatore = new Organizzatore("Mario", "Rossi");
        Giudice giudice = new Giudice("Luigi", "Verdi");
        DatiHackathon dati = new DatiHackathon(
                "Hackathon Test",
                "Roma",
                new MoneyAmount(new BigDecimal("1000.00"), Currency.getInstance("EUR")),
                5,
                "Regolamento test"
        );
        LocalDateTime now = LocalDateTime.now();
        Intervallo iscrizioni = new Intervallo(now.plusDays(1), now.plusDays(10));
        Intervallo durata = new Intervallo(now.plusDays(15), now.plusDays(17));

        Hackathon hackathon = new Hackathon(organizzatore, dati, giudice, iscrizioni, durata);
        when(hackathonRepository.save(any(Hackathon.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Verify initial state
        assertEquals(RappresentazioneStato.ISCRIZIONE, hackathon.getRappresentazioneStato());

        // Act
        hackathonManager.avanzaStato(hackathon);

        // Assert
        assertEquals(RappresentazioneStato.IN_CORSO, hackathon.getRappresentazioneStato());
        verify(hackathonRepository).save(hackathon);
    }
}
