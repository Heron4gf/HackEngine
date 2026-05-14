package it.unicam.ids2026.slice;

import it.unicam.ids2026.api.controller.SupportRequestController;
import it.unicam.ids2026.core.hackathon.Hackathon;
import it.unicam.ids2026.core.managers.HackathonManager;
import it.unicam.ids2026.core.managers.SupportRequestManager;
import it.unicam.ids2026.core.managers.TeamManager;
import it.unicam.ids2026.core.managers.UserManager;
import it.unicam.ids2026.core.roles.team.Team;
import it.unicam.ids2026.core.supportRequest.RichiestaSupporto;
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

@WebMvcTest(SupportRequestController.class)
class SupportRequestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private SupportRequestManager supportRequestManager;

    @MockitoBean
    private HackathonManager hackathonManager;

    @MockitoBean
    private TeamManager teamManager;

    @MockitoBean
    private UserManager userManager;

    @Test
    void creaRichiestaSupport_ValidBody_ShouldReturn201() throws Exception {
        UUID hackathonId = UUID.randomUUID();
        String nomeTeam = "Team Test";
        Hackathon hackathon = mock(Hackathon.class);
        Team team = new Team(nomeTeam, 5);
        RichiestaSupporto richiesta = new RichiestaSupporto("Build bloccata", "La pipeline fallisce in test");

        when(hackathonManager.getHackathon(hackathonId)).thenReturn(hackathon);
        when(teamManager.getTeam(nomeTeam)).thenReturn(team);
        when(supportRequestManager.creaRichiestaSupporto(
                hackathon,
                team,
                "Build bloccata",
                "La pipeline fallisce in test"
        )).thenReturn(richiesta);

        String requestBody = String.format("""
            {
                "nomeTeam": "%s",
                "titolo": "Build bloccata",
                "descrizione": "La pipeline fallisce in test"
            }
            """, nomeTeam);

        mockMvc.perform(post("/api/hackathons/{hackathonId}/support-requests", hackathonId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.titolo", is("Build bloccata")))
            .andExpect(jsonPath("$.stato", is("IN_ATTESA")));
    }

    @Test
    void getRichieste_ShouldReturnHackathonSupportRequests() throws Exception {
        UUID hackathonId = UUID.randomUUID();
        Hackathon hackathon = mock(Hackathon.class);
        RichiestaSupporto richiesta = new RichiestaSupporto("Deploy", "Serve supporto per il deploy");

        when(hackathonManager.getHackathon(hackathonId)).thenReturn(hackathon);
        when(supportRequestManager.visualizzaRichieste(hackathon)).thenReturn(Set.of(richiesta));

        mockMvc.perform(get("/api/hackathons/{hackathonId}/support-requests", hackathonId))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$", hasSize(1)))
            .andExpect(jsonPath("$[0].titolo", is("Deploy")));
    }
}
