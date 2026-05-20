package it.unicam.ids2026.unit;

import it.unicam.ids2026.api.events.HackathonChangeListener;
import it.unicam.ids2026.api.events.TeamDeletionListener;
import it.unicam.ids2026.api.events.UserChangeListener;
import it.unicam.ids2026.core.events.EventPublisher;
import it.unicam.ids2026.core.hackathon.Hackathon;
import it.unicam.ids2026.core.roles.team.Team;
import it.unicam.ids2026.core.roles.team.Utente;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EventPublisherTest {

    private EventPublisher eventPublisher;

    @Mock
    private TeamDeletionListener teamDeletionListener;

    @BeforeEach
    void setUp() {
        eventPublisher = new EventPublisher();
    }

    @Test
    void publishTeamDeletion_WithRegisteredListener_ShouldCallListener() {
        // Arrange
        eventPublisher.registerListener(teamDeletionListener);
        Team team = new Team("Team Test", 5);

        // Act
        eventPublisher.publishTeamDeletion(team);

        // Assert
        verify(teamDeletionListener).notifyTeamDeletion(team);
    }

    @Test
    void publishTeamDeletion_WithNoListeners_ShouldNotThrow() {
        // Arrange
        Team team = new Team("Team Test", 5);

        // Act & Assert - should not throw any exception
        assertDoesNotThrow(() -> eventPublisher.publishTeamDeletion(team));
    }

    @Test
    void publishTeamDeletion_WithMultipleListeners_ShouldCallAllListeners() {
        // Arrange
        TeamDeletionListener listener1 = mock(TeamDeletionListener.class);
        TeamDeletionListener listener2 = mock(TeamDeletionListener.class);
        eventPublisher.registerListener(listener1);
        eventPublisher.registerListener(listener2);
        Team team = new Team("Team Test", 5);

        // Act
        eventPublisher.publishTeamDeletion(team);

        // Assert
        verify(listener1).notifyTeamDeletion(team);
        verify(listener2).notifyTeamDeletion(team);
    }

    @Test
    void deleteListener_AfterDeletion_ShouldNotCallListener() {
        // Arrange
        eventPublisher.registerListener(teamDeletionListener);
        eventPublisher.deleteListener(teamDeletionListener);
        Team team = new Team("Team Test", 5);

        // Act
        eventPublisher.publishTeamDeletion(team);

        // Assert
        verify(teamDeletionListener, never()).notifyTeamDeletion(any());
    }

    @Test
    void publishHackathonChange_WithRegisteredListener_ShouldCallListener() {
        // Arrange
        HackathonChangeListener listener = mock(HackathonChangeListener.class);
        Hackathon hackathon = mock(Hackathon.class);
        eventPublisher.registerListener(listener);

        // Act
        eventPublisher.publishHackathonChange(hackathon);

        // Assert
        verify(listener).notifyHackathonChange(hackathon);
    }

    @Test
    void publishUserChange_WithRegisteredListener_ShouldCallListener() {
        // Arrange
        UserChangeListener listener = mock(UserChangeListener.class);
        Utente utente = new Utente("Mario Rossi");
        eventPublisher.registerListener(listener);

        // Act
        eventPublisher.publishUserChange(utente);

        // Assert
        verify(listener).notifyUserChange(utente);
    }
}
