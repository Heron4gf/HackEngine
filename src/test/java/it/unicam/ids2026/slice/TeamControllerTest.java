package it.unicam.ids2026.slice;

import it.unicam.ids2026.api.controller.TeamController;
import it.unicam.ids2026.core.hackathon.Hackathon;
import it.unicam.ids2026.core.managers.HackathonManager;
import it.unicam.ids2026.core.managers.TeamManager;
import it.unicam.ids2026.core.managers.UserManager;
import it.unicam.ids2026.core.roles.staff.Organizzatore;
import it.unicam.ids2026.core.roles.team.Team;
import it.unicam.ids2026.core.roles.team.Utente;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;
import java.util.Set;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(TeamController.class)
class TeamControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private TeamManager teamManager;

    @MockitoBean
    private UserManager userManager;

    @MockitoBean
    private HackathonManager hackathonManager;

    @Test
    void createTeam_ValidBody_ShouldReturn201() throws Exception {
        // Arrange
        UUID utenteId = UUID.randomUUID();
        Utente utente = new Utente(utenteId, "Mario Rossi", null);
        Team team = new Team("Team Test", 5);

        when(userManager.getUserById(utenteId)).thenReturn(utente);
        doNothing().when(teamManager).creaTeam(any(), any(), anyInt());
        when(teamManager.getTeam("Team Test")).thenReturn(team);

        String requestBody = String.format("""
            {
                "utenteId": "%s",
                "nome": "Team Test",
                "maxMembri": 5
            }
            """, utenteId);

        // Act & Assert
        mockMvc.perform(post("/api/teams")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.nome", is("Team Test")));
    }

    @Test
    void createTeam_InvalidUtenteIdWrongRole_ShouldReturn400() throws Exception {
        // Arrange
        UUID utenteId = UUID.randomUUID();

        when(userManager.getUserById(utenteId)).thenReturn(new Organizzatore("Mario", "Rossi"));

        String requestBody = String.format("""
            {
                "utenteId": "%s",
                "nome": "Team Test",
                "maxMembri": 5
            }
            """, utenteId);

        // Act & Assert
        mockMvc.perform(post("/api/teams")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
            .andExpect(status().isBadRequest());
    }

    @Test
    void getTeam_ValidNome_ShouldReturn200() throws Exception {
        // Arrange
        String teamName = "Team Test";
        Team team = new Team(teamName, 5);

        when(teamManager.getTeam(teamName)).thenReturn(team);

        // Act & Assert
        mockMvc.perform(get("/api/teams/{nome}", teamName))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.nome", is(teamName)));
    }

    @Test
    void getTeams_WithHackathonId_ShouldReturnOnlyHackathonTeams() throws Exception {
        // Arrange
        UUID hackathonId = UUID.randomUUID();
        Hackathon hackathon = mock(Hackathon.class);
        Team team = new Team("Scoped Team", 5);

        when(hackathonManager.getHackathon(hackathonId)).thenReturn(hackathon);
        when(hackathon.getTeams()).thenReturn(Set.of(team));

        // Act & Assert
        mockMvc.perform(get("/api/teams")
                .param("hackathonId", hackathonId.toString()))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$", hasSize(1)))
            .andExpect(jsonPath("$[0].nome", is("Scoped Team")));

        verify(teamManager, never()).getTeams();
    }

    @Test
    void exitTeam_ShouldReturn200() throws Exception {
        // Arrange
        String teamName = "Team Test";
        UUID utenteId = UUID.randomUUID();
        Utente utente = new Utente(utenteId, "Mario Rossi", null);

        when(userManager.getUserById(utenteId)).thenReturn(utente);
        doNothing().when(teamManager).esciDalTeam(utente);

        // Act & Assert
        mockMvc.perform(post("/api/teams/{nome}/esci", teamName)
                .param("utenteId", utenteId.toString()))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.message", is("Uscito dal team con successo")));
    }

    @Test
    void iscriviTeam_UtenteNotInTeam_ShouldReturn400() throws Exception {
        // Arrange
        String teamName = "Team Test";
        UUID hackathonId = UUID.randomUUID();
        UUID utenteId = UUID.randomUUID();

        Team team = new Team(teamName, 5);
        Utente utente = new Utente(utenteId, "Mario Rossi", new Team("Altro Team", 5)); // Different team

        when(teamManager.getTeam(teamName)).thenReturn(team);
        when(userManager.getUserById(utenteId)).thenReturn(utente);
        when(hackathonManager.getHackathon(hackathonId)).thenReturn(mock(Hackathon.class));
        doThrow(new IllegalArgumentException("L'utente indicato non appartiene al team " + teamName))
                .when(hackathonManager).iscriviTeam(any(), eq(team), eq(utente));

        // Act & Assert
        mockMvc.perform(post("/api/teams/{nome}/iscrizione", teamName)
                .param("hackathonId", hackathonId.toString())
                .param("utenteId", utenteId.toString()))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.message", containsString("non appartiene al team")));
    }
}
