package it.unicam.ids2026.slice;

import it.unicam.ids2026.api.controller.ViolationController;
import it.unicam.ids2026.core.hackathon.Hackathon;
import it.unicam.ids2026.core.managers.HackathonManager;
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

    @Test
    void segnalaTeam_ValidBody_ShouldReturn201() throws Exception {
        UUID hackathonId = UUID.randomUUID();
        UUID mentoreId = UUID.randomUUID();
        UUID teamId = UUID.randomUUID();
        Hackathon hackathon = mock(Hackathon.class);
        Mentore mentore = new Mentore(mentoreId, "Mario", "Rossi", Set.of());
        Team team = mock(Team.class);
        Violazione violazione = new Violazione(mentore, team, hackathon, "Uso improprio del repository");

        when(hackathon.getId()).thenReturn(hackathonId);
        when(team.getId()).thenReturn(teamId);
        when(violationManager.segnalaTeam(hackathonId, mentoreId, teamId, "Uso improprio del repository"))
                .thenReturn(violazione);

        String requestBody = String.format("""
            {
                "mentoreId": "%s",
                "teamId": "%s",
                "descrizione": "Uso improprio del repository"
            }
            """, mentoreId, teamId);

        mockMvc.perform(post("/api/hackathons/{hackathonId}/violations", hackathonId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.hackathonId", is(hackathonId.toString())))
            .andExpect(jsonPath("$.teamId", is(teamId.toString())))
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
