package it.unicam.ids2026.slice;

import it.unicam.ids2026.api.controller.InviteController;
import it.unicam.ids2026.core.managers.InviteManager;
import it.unicam.ids2026.core.managers.TeamManager;
import it.unicam.ids2026.core.managers.UserManager;
import it.unicam.ids2026.core.roles.team.Invito;
import it.unicam.ids2026.core.roles.team.Team;
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

@WebMvcTest(InviteController.class)
class InviteControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private InviteManager inviteManager;

    @MockitoBean
    private TeamManager teamManager;

    @MockitoBean
    private UserManager userManager;

    @Test
    void inviteUser_ValidRequest_ShouldReturn200() throws Exception {
        // Arrange
        String teamName = "Team Test";
        UUID destinatarioId = UUID.randomUUID();
        Team team = new Team(teamName, 5);
        Utente destinatario = new Utente(destinatarioId, "Mario Rossi", null);

        when(teamManager.getTeam(teamName)).thenReturn(team);
        when(userManager.getUserById(destinatarioId)).thenReturn(destinatario);
        doNothing().when(inviteManager).invitaUtente(any(), any());

        String requestBody = String.format("""
            {
                "teamMittenteNome": "%s",
                "destinatarioId": "%s"
            }
            """, teamName, destinatarioId);

        // Act & Assert
        mockMvc.perform(post("/api/invites")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.message", is("Invito inviato con successo")));
    }

    @Test
    void accettaInvito_NonExistentInvite_ShouldReturn404() throws Exception {
        // Arrange
        String teamName = "Team Test";
        UUID utenteId = UUID.randomUUID();
        Team team = new Team(teamName, 5);
        Utente utente = new Utente(utenteId, "Mario Rossi", null);

        when(teamManager.getTeam(teamName)).thenReturn(team);
        when(userManager.getUserById(utenteId)).thenReturn(utente);
        when(inviteManager.findInvito(team, utente)).thenReturn(null);

        // Act & Assert - This will throw NullPointerException when trying to accept null invite
        // In real implementation, InviteManager should throw NoSuchElementException
        mockMvc.perform(post("/api/invites/accetta")
                .param("nomeTeam", teamName)
                .param("utenteId", utenteId.toString()))
            .andExpect(status().isNotFound());
    }

    @Test
    void getCasellaInviti_ShouldReturn200() throws Exception {
        // Arrange
        UUID utenteId = UUID.randomUUID();
        Utente utente = new Utente(utenteId, "Mario Rossi", null);

        when(userManager.getUserById(utenteId)).thenReturn(utente);
        when(inviteManager.getCasellaInviti(utente)).thenReturn(Set.of());

        // Act & Assert
        mockMvc.perform(get("/api/invites/casella/{utenteId}", utenteId))
            .andExpect(status().isOk());
    }

    @Test
    void rifiutaInvito_ShouldReturn200() throws Exception {
        // Arrange
        String teamName = "Team Test";
        UUID utenteId = UUID.randomUUID();
        Team team = new Team(teamName, 5);
        Utente utente = new Utente(utenteId, "Mario Rossi", null);
        Invito invito = new Invito(team, utente);

        when(teamManager.getTeam(teamName)).thenReturn(team);
        when(userManager.getUserById(utenteId)).thenReturn(utente);
        when(inviteManager.findInvito(team, utente)).thenReturn(invito);
        doNothing().when(inviteManager).rifiutaInvito(invito);

        // Act & Assert
        mockMvc.perform(post("/api/invites/rifiuta")
                .param("nomeTeam", teamName)
                .param("utenteId", utenteId.toString()))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.message", is("Invito rifiutato")));
    }
}
