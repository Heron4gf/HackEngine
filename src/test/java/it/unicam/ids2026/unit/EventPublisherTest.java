package it.unicam.ids2026.unit;

import it.unicam.ids2026.api.events.DeletionListener;
import it.unicam.ids2026.core.events.EventPublisher;
import it.unicam.ids2026.core.roles.team.Team;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EventPublisherTest {

    private EventPublisher eventPublisher;

    @Mock
    private DeletionListener deletionListener;

    @BeforeEach
    void setUp() {
        eventPublisher = new EventPublisher();
    }

    @Test
    void publishDeletion_WithRegisteredListener_ShouldCallListener() {
        // Arrange
        eventPublisher.registerListener(deletionListener);
        Team team = new Team("Team Test", 5);

        // Act
        eventPublisher.publishDeletion(team);

        // Assert
        verify(deletionListener).notifyDeletion(team);
    }

    @Test
    void publishDeletion_WithNoListeners_ShouldNotThrow() {
        // Arrange
        Team team = new Team("Team Test", 5);

        // Act & Assert - should not throw any exception
        assertDoesNotThrow(() -> eventPublisher.publishDeletion(team));
    }

    @Test
    void publishDeletion_WithMultipleListeners_ShouldCallAllListeners() {
        // Arrange
        DeletionListener listener1 = mock(DeletionListener.class);
        DeletionListener listener2 = mock(DeletionListener.class);
        eventPublisher.registerListener(listener1);
        eventPublisher.registerListener(listener2);
        Team team = new Team("Team Test", 5);

        // Act
        eventPublisher.publishDeletion(team);

        // Assert
        verify(listener1).notifyDeletion(team);
        verify(listener2).notifyDeletion(team);
    }

    @Test
    void deleteListener_AfterDeletion_ShouldNotCallListener() {
        // Arrange
        eventPublisher.registerListener(deletionListener);
        eventPublisher.deleteListener(deletionListener);
        Team team = new Team("Team Test", 5);

        // Act
        eventPublisher.publishDeletion(team);

        // Assert
        verify(deletionListener, never()).notifyDeletion(any());
    }
}
