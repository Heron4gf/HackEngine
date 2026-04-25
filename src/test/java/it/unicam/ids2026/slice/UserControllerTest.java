package it.unicam.ids2026.slice;

import it.unicam.ids2026.api.controller.UserController;
import it.unicam.ids2026.core.managers.UserManager;
import it.unicam.ids2026.core.roles.team.Utente;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.NoSuchElementException;
import java.util.Set;
import java.util.UUID;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserController.class)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private UserManager userManager;

    @Test
    void createUser_ShouldReturn201() throws Exception {
        // Arrange
        UUID userId = UUID.randomUUID();
        Utente user = new Utente(userId, "Mario Rossi", null);

        doNothing().when(userManager).addUser(any());

        String requestBody = """
            {
                "role": "UTENTE",
                "nome": "Mario Rossi"
            }
            """;

        // Act & Assert
        mockMvc.perform(post("/api/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.nome", is("Mario Rossi")))
            .andExpect(jsonPath("$.role", is("Utente")));
    }

    @Test
    void getUser_ValidId_ShouldReturn200() throws Exception {
        // Arrange
        UUID userId = UUID.randomUUID();
        Utente user = new Utente(userId, "Mario Rossi", null);

        when(userManager.getUserById(userId)).thenReturn(user);

        // Act & Assert
        mockMvc.perform(get("/api/users/{id}", userId))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id", is(userId.toString())))
            .andExpect(jsonPath("$.nome", is("Mario Rossi")));
    }

    @Test
    void getUser_UnknownId_ShouldReturn404() throws Exception {
        // Arrange
        UUID unknownId = UUID.randomUUID();
        when(userManager.getUserById(unknownId))
            .thenThrow(new NoSuchElementException("Nessun utente trovato con ID: " + unknownId));

        // Act & Assert
        mockMvc.perform(get("/api/users/{id}", unknownId))
            .andExpect(status().isNotFound())
            .andExpect(jsonPath("$.message", containsString("Nessun utente trovato")));
    }

    @Test
    void getAllUsers_ShouldReturn200() throws Exception {
        // Arrange
        when(userManager.getUsers()).thenReturn(Set.of());

        // Act & Assert
        mockMvc.perform(get("/api/users"))
            .andExpect(status().isOk());
    }
}
