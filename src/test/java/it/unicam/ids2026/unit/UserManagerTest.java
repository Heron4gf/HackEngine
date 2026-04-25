package it.unicam.ids2026.unit;

import it.unicam.ids2026.core.managers.UserManager;
import it.unicam.ids2026.core.roles.User;
import it.unicam.ids2026.core.roles.team.Utente;
import it.unicam.ids2026.persistence.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserManagerTest {

    @Mock
    private UserRepository userRepository;

    private UserManager userManager;

    @BeforeEach
    void setUp() {
        userManager = new UserManager(userRepository);
    }

    @Test
    void addUser_HappyPath_ShouldStoreInRepo() {
        // Arrange
        Utente user = new Utente("Mario Rossi");
        when(userRepository.existsById(user.getId())).thenReturn(false);
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        userManager.addUser(user);

        // Assert
        verify(userRepository).save(user);
    }

    @Test
    void addUser_DuplicateUser_ShouldThrowIllegalArgumentException() {
        // Arrange
        Utente user = new Utente("Mario Rossi");
        when(userRepository.existsById(user.getId())).thenReturn(true);

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
            userManager.addUser(user)
        );
        assertEquals("Utente gia presente nel sistema", exception.getMessage());
    }

    @Test
    void getUserById_UnknownId_ShouldThrowNoSuchElementException() {
        // Arrange
        UUID unknownId = UUID.randomUUID();
        when(userRepository.findById(unknownId)).thenReturn(Optional.empty());

        // Act & Assert
        NoSuchElementException exception = assertThrows(NoSuchElementException.class, () ->
            userManager.getUserById(unknownId)
        );
        assertTrue(exception.getMessage().contains("Nessun utente trovato con ID"));
        assertTrue(exception.getMessage().contains(unknownId.toString()));
    }

    @Test
    void getUserById_ExistingId_ShouldReturnUser() {
        // Arrange
        Utente user = new Utente("Mario Rossi");
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));

        // Act
        User result = userManager.getUserById(user.getId());

        // Assert
        assertNotNull(result);
        assertEquals(user.getId(), result.getId());
        assertEquals("Mario Rossi", result.getNome());
    }
}
