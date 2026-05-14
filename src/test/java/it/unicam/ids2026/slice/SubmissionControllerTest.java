package it.unicam.ids2026.slice;

import it.unicam.ids2026.api.controller.SubmissionController;
import it.unicam.ids2026.core.hackathon.Hackathon;
import it.unicam.ids2026.core.hackathon.data.Sottomissione;
import it.unicam.ids2026.core.managers.HackathonManager;
import it.unicam.ids2026.core.managers.SubmissionManager;
import it.unicam.ids2026.core.managers.TeamManager;
import it.unicam.ids2026.core.roles.team.Team;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.io.File;
import java.util.*;

import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(SubmissionController.class)
public class SubmissionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private SubmissionManager submissionManager;

    @MockitoBean
    private HackathonManager hackathonManager;

    @MockitoBean
    private TeamManager teamManager;

    @Test
    void submissionValidSend() throws Exception {
        UUID hackathonID = UUID.randomUUID();
        String teamName = "MioTeam";
        String nome = "Nome";
        String descrizione = "Descrizione valida";
        File allegato = new File("Path").getAbsoluteFile();

        Sottomissione sottomissione = new Sottomissione(nome, descrizione, allegato);

        Hackathon hackathon = mock(Hackathon.class);
        Team team = mock(Team.class);

        when(hackathonManager.getHackathon(hackathonID)).thenReturn(hackathon);
        when(teamManager.getTeam(teamName)).thenReturn(team);
        when(submissionManager.inviaSottomissione(hackathon, team, nome, descrizione, allegato)).thenReturn(sottomissione);

        String requestBody = String.format(
                """
                   {
                        "name": "%s",
                        "descrizione": "%s",
                        "allegato": "%s"
                   }
                   """,
                nome,
                descrizione,
                allegato.getAbsolutePath()
        );

        mockMvc.perform(post("/api/hackathons/{hackathonId}/teams/{teamId}/submission", hackathonID, teamName)
                .contentType(APPLICATION_JSON)
                .content(requestBody))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.nome", is(nome)))
                .andExpect(jsonPath("$.descrizione", is(descrizione)))
                .andExpect(jsonPath("$.allegato", is(allegato.getAbsolutePath())));
    }

    @Test
    void submissionValidUpdate() throws Exception {
        UUID hackathonUUID = UUID.randomUUID();
        String teamName = "MioTeam";
        String nome = "Progetto";
        String descrizione = "Descrizione valida";
        File allegato = new File("Path").getAbsoluteFile();
        Sottomissione sottomissione = new Sottomissione(nome, descrizione, allegato);

        Hackathon hackathon = mock(Hackathon.class);
        Team team = mock(Team.class);

        when(hackathonManager.getHackathon(hackathonUUID)).thenReturn(hackathon);
        when(teamManager.getTeam(teamName)).thenReturn(team);

        String descrizione1 = "Nuova Descrizione";
        File allegato1 = new File("NewPath").getAbsoluteFile();
        Sottomissione nuovaSottomissione = new Sottomissione(nome, descrizione1, allegato1);

        when(submissionManager.aggiornaSottomissione(hackathon, team, descrizione1, allegato1)).thenReturn(nuovaSottomissione);

        String requestBody = String.format(
                """
                        {
                            "descrizione": "%s",
                            "allegato": "%s"
                        }""",
                descrizione1,
                allegato1.getAbsolutePath()
        );

        mockMvc.perform(put("/api/hackathons/{hackathonId}/teams/{teamId}/submission", hackathonUUID, teamName)
                .contentType(APPLICATION_JSON)
                .content(requestBody))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nome", is(nome)))
                .andExpect(jsonPath("$.descrizione", is(descrizione1)))
                .andExpect(jsonPath("$.allegato", is(allegato1.getAbsolutePath())));
    }

    @Test
    void submissionInvalidUpdateBlackDescription() throws Exception {
        UUID hackathonUUID = UUID.randomUUID();
        String teamName = "MioTeam";
        String nome = "Progetto";
        String descrizione = "Descrizione valida";
        File allegato = new File("Path").getAbsoluteFile();

        Hackathon hackathon = mock(Hackathon.class);
        Team team = mock(Team.class);

        when(hackathonManager.getHackathon(hackathonUUID)).thenReturn(hackathon);
        when(teamManager.getTeam(teamName)).thenReturn(team);

        String descrizione1 = "";
        File allegato1 = new File("NewPath").getAbsoluteFile();

        when(submissionManager.aggiornaSottomissione(hackathon, team, descrizione1, allegato1))
                .thenThrow(new IllegalArgumentException("I dati della sottomissione non sono validi"));

        String requestBody = String.format(
                """
                    {
                      "descrizione": "%s",
                      "allegato": "%s"
                    }
                    """,
                descrizione1,
                allegato1.getAbsolutePath()
        );

        mockMvc.perform(put("/api/hackathons/{hackathonId}/teams/{teamId}/submission", hackathonUUID, teamName)
                .contentType(APPLICATION_JSON)
                .content(requestBody))
                .andExpect(status().isBadRequest());
    }

    @Test
    void obtainingSubmission() throws Exception {
        UUID hackathonUUID = UUID.randomUUID();
        String teamName = "MioTeam";
        String nome = "Progetto";
        String descrizione = "Descrizione valida";
        File allegato = new File("Path").getAbsoluteFile();
        Sottomissione sottomissione = new Sottomissione(nome, descrizione, allegato);

        Hackathon hackathon = mock(Hackathon.class);
        Team team = mock(Team.class);

        when(hackathonManager.getHackathon(hackathonUUID)).thenReturn(hackathon);
        when(teamManager.getTeam(teamName)).thenReturn(team);
        when(submissionManager.ottieniSottomissione(hackathon, team)).thenReturn(sottomissione);

        mockMvc.perform(get("/api/hackathons/{hackathonId}/teams/{teamId}/submission", hackathonUUID, teamName)
                .contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nome", is(nome)))
                .andExpect(jsonPath("$.descrizione", is(descrizione)))
                .andExpect(jsonPath("$.allegato", is(allegato.getAbsolutePath())));
    }

    @Test
    void obtainingNonExistentSubmission() throws Exception {
        UUID hackathonUUID = UUID.randomUUID();
        String teamName = "MioTeam";

        Hackathon hackathon = mock(Hackathon.class);
        Team team = mock(Team.class);

        when(hackathonManager.getHackathon(hackathonUUID)).thenReturn(hackathon);
        when(teamManager.getTeam(teamName)).thenReturn(team);
        when(submissionManager.ottieniSottomissione(hackathon, team))
                .thenThrow(new NoSuchElementException("Non esiste una sottomissione associata a questo team"));

        mockMvc.perform(get("/api/hackathons/{hackathonId}/teams/{teamId}/submission", hackathonUUID, teamName)
                .contentType(APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }
}
