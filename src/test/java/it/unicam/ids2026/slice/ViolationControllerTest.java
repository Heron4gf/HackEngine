package it.unicam.ids2026.slice;

import it.unicam.ids2026.api.controller.ViolationController;
import it.unicam.ids2026.core.hackathon.Hackathon;
import it.unicam.ids2026.core.managers.HackathonManager;
import it.unicam.ids2026.core.managers.TeamManager;
import it.unicam.ids2026.core.managers.UserManager;
import it.unicam.ids2026.core.managers.ViolationManager;
import it.unicam.ids2026.core.roles.staff.Mentore;
import it.unicam.ids2026.core.roles.team.Team;
import it.unicam.ids2026.core.violation.Violazione;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Set;
import java.util.UUID;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ViolationController.class)
class ViolationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ViolationManager violationManager;

    @MockitoBean
    private HackathonManager hackathonManager;

    @MockitoBean
    private TeamManager teamManager;

    @MockitoBean
    private UserManager userManager;

    @Test
    void segnalaTeam_ValidBody_ShouldReturn201() throws Exception {
        UUID hackathonId = UUID.randomUUID();
        String nomeTeam = "Team Test";
        Hackathon hackathon = mock(Hackathon.class);
        Mentore mentore = new Mentore("Mario", "Rossi");
        UUID mentoreId = mentore.getId();
        Team team = new Team(nomeTeam, 5);
        Violazione violazione = new Violazione(mentore, team, hackathon, "Uso improprio del repository");

        when(hackathon.getId()).thenReturn(hackathonId);
        when(hackathonManager.getHackathon(hackathonId)).thenReturn(hackathon);
        when(userManager.getUserById(mentoreId)).thenReturn(mentore);
        when(teamManager.getTeam(nomeTeam)).thenReturn(team);
        when(violationManager.segnalaTeam(hackathon, team, mentore, "Uso improprio del repository"))
                .thenReturn(violazione);

        String requestBody = String.format("""
            {
                "mentoreId": "%s",
                "nomeTeam": "%s",
                "descrizione": "Uso improprio del repository"
            }
            """, mentoreId, nomeTeam);

        mockMvc.perform(post("/api/hackathons/{hackathonId}/violations", hackathonId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.hackathonId", is(hackathonId.toString())))
            .andExpect(jsonPath("$.nomeTeam", is(nomeTeam)))
            .andExpect(jsonPath("$.stato", is("SOLLEVATA")));
    }

    @Test
    void getViolations_ShouldReturnHackathonViolations() throws Exception {
        UUID hackathonId = UUID.randomUUID();
        Hackathon hackathon = mock(Hackathon.class);
        Mentore mentore = new Mentore("Mario", "Rossi");
        Team team = new Team("Team Test", 5);
        Violazione violazione = new Violazione(mentore, team, hackathon, "Uso improprio del repository");

        when(hackathon.getId()).thenReturn(hackathonId);
        when(hackathonManager.getHackathon(hackathonId)).thenReturn(hackathon);
        when(violationManager.getViolations(hackathon)).thenReturn(Set.of(violazione));

        mockMvc.perform(get("/api/hackathons/{hackathonId}/violations", hackathonId))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$", hasSize(1)))
            .andExpect(jsonPath("$[0].descrizione", is("Uso improprio del repository")));
    }
}
