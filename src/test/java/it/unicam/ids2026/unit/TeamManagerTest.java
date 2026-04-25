package it.unicam.ids2026.unit;

import it.unicam.ids2026.core.events.EventPublisher;
import it.unicam.ids2026.core.managers.TeamManager;
import it.unicam.ids2026.core.roles.team.Team;
import it.unicam.ids2026.core.roles.team.Utente;
import it.unicam.ids2026.persistence.TeamRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashSet;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TeamManagerTest {

    @Mock
    private TeamRepository teamRepository;

    @Mock
    private EventPublisher eventPublisher;

    private TeamManager teamManager;

    @BeforeEach
    void setUp() {
        teamManager = new TeamManager(teamRepository, eventPublisher);
    }

    @Test
    void creaTeam_HappyPath_ShouldCreateTeamAndAssignUtente() {
        // Arrange
        Utente utente = new Utente("Mario Rossi");
        String teamName = "Team Test";
        int maxMembri = 5;

        when(teamRepository.existsById(teamName)).thenReturn(false);
        when(teamRepository.save(any(Team.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        teamManager.creaTeam(utente, teamName, maxMembri);

        // Assert
        assertTrue(utente.haTeam());
        assertEquals(teamName, utente.getTeam().getNome());
        verify(teamRepository).save(any(Team.class));
    }

    @Test
    void creaTeam_DuplicateTeamName_ShouldThrowIllegalArgumentException() {
        // Arrange
        Utente utente = new Utente("Mario Rossi");
        String teamName = "Team Esistente";
        int maxMembri = 5;

        when(teamRepository.existsById(teamName)).thenReturn(true);

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
            teamManager.creaTeam(utente, teamName, maxMembri)
        );
        assertEquals("Esiste gia un team con lo stesso nome", exception.getMessage());
    }

    @Test
    void esciDalTeam_UtenteHasNoTeam_ShouldThrowIllegalArgumentException() {
        // Arrange
        Utente utenteSenzaTeam = new Utente("Mario Rossi");

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
            teamManager.esciDalTeam(utenteSenzaTeam)
        );
        assertEquals("L'Utente non ha team!", exception.getMessage());
    }

    @Test
    void esciDalTeam_TeamBecomesEmpty_ShouldPublishDeletionEvent() {
        // Arrange
        Team team = new Team("Team Test", 5);
        Utente utente = new Utente(UUID.randomUUID(), "Mario Rossi", team);
        team.getMembri().add(utente);

        // Act
        teamManager.esciDalTeam(utente);

        // Assert
        assertFalse(utente.haTeam());
        verify(eventPublisher).publishDeletion(team);
        verify(teamRepository).delete(team);
    }

    @Test
    void esciDalTeam_TeamStillHasMembers_ShouldSaveTeam() {
        // Arrange
        Team team = new Team("Team Test", 5);
        Utente utente1 = new Utente(UUID.randomUUID(), "Mario Rossi", team);
        Utente utente2 = new Utente(UUID.randomUUID(), "Luigi Verdi", team);
        team.getMembri().add(utente1);
        team.getMembri().add(utente2);

        // Act
        teamManager.esciDalTeam(utente1);

        // Assert
        assertFalse(utente1.haTeam());
        assertTrue(team.getMembri().contains(utente2));
        verify(teamRepository).save(team);
        verify(eventPublisher, never()).publishDeletion(any());
        verify(teamRepository, never()).delete(any());
    }
}
