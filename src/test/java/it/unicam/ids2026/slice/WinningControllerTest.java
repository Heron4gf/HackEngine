package it.unicam.ids2026.slice;

import it.unicam.ids2026.api.controller.WinningController;
import it.unicam.ids2026.api.dto.request.AssegnaVincitoreRequest;
import it.unicam.ids2026.core.hackathon.Hackathon;
import it.unicam.ids2026.core.hackathon.data.DatiHackathon;
import it.unicam.ids2026.core.hackathon.data.Intervallo;
import it.unicam.ids2026.core.hackathon.data.Sottomissione;
import it.unicam.ids2026.core.hackathon.data.Valutazione;
import it.unicam.ids2026.core.hackathon.status.StatoInValutazione;
import it.unicam.ids2026.core.managers.HackathonManager;
import it.unicam.ids2026.core.managers.TeamManager;
import it.unicam.ids2026.core.managers.WinningManager;
import it.unicam.ids2026.core.roles.staff.Giudice;
import it.unicam.ids2026.core.roles.staff.Organizzatore;
import it.unicam.ids2026.core.roles.team.Iscrizione;
import it.unicam.ids2026.core.roles.team.Team;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.json.JsonMapper;

import java.io.File;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Currency;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test slice per WinningController.
 */
@WebMvcTest(WinningController.class)
class WinningControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private final JsonMapper jsonMapper = new JsonMapper();

    @MockitoBean
    private WinningManager winningManager;

    @MockitoBean
    private HackathonManager hackathonManager;

    @MockitoBean
    private TeamManager teamManager;

    private Hackathon hackathon;
    private Team team1;
    private Team team2;
    private UUID hackathonId;

    @BeforeEach
    void setUp() {
        Organizzatore organizzatore = new Organizzatore("Mario", "Rossi");
        Giudice giudice = new Giudice("Luigi", "Verdi");
        DatiHackathon dati = new DatiHackathon(
                "Hackathon Test",
                "Roma",
                new it.unicam.ids2026.core.transaction.MoneyAmount(new BigDecimal("1000.00"), Currency.getInstance("EUR")),
                5,
                "Regolamento test"
        );
        LocalDateTime now = LocalDateTime.now();
        Intervallo iscrizioni = new Intervallo(now.plusDays(1), now.plusDays(10));
        Intervallo durata = new Intervallo(now.plusDays(15), now.plusDays(17));

        hackathon = new Hackathon(organizzatore, dati, giudice, iscrizioni, durata);
        hackathonId = hackathon.getId();
        hackathon.setState(new StatoInValutazione());

        team1 = new Team("Team Alpha", 5);
        team2 = new Team("Team Beta", 5);

        // Aggiungi iscrizioni valutate
        hackathon.getIscritti().put(team1, createIscrizioneValutata(team1, 9));
        hackathon.getIscritti().put(team2, createIscrizioneValutata(team2, 8));
    }

    @Test
    void getWinnerCandidates_AllValutate_RestituisceListaCandidati() throws Exception {
        // Arrange
        when(hackathonManager.getHackathon(hackathonId)).thenReturn(hackathon);
        when(winningManager.ottieniTeamConPunteggioMassimo(hackathon))
                .thenReturn(List.of(hackathon.getIscritti().get(team1)));

        // Act & Assert
        mockMvc.perform(get("/api/hackathons/{hackathonId}/winner-candidates", hackathonId))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].teamName", is("Team Alpha")))
                .andExpect(jsonPath("$[0].punteggio", is(9.0)));
    }

    @Test
    void getWinnerCandidates_Pareggio_RestituisceTuttiCandidati() throws Exception {
        // Arrange - Modifica i punteggi per avere pareggio
        hackathon.getIscritti().put(team1, createIscrizioneValutata(team1, 8));
        hackathon.getIscritti().put(team2, createIscrizioneValutata(team2, 8));

        when(hackathonManager.getHackathon(hackathonId)).thenReturn(hackathon);
        when(winningManager.ottieniTeamConPunteggioMassimo(hackathon))
                .thenReturn(List.of(
                        hackathon.getIscritti().get(team1),
                        hackathon.getIscritti().get(team2)
                ));

        // Act & Assert
        mockMvc.perform(get("/api/hackathons/{hackathonId}/winner-candidates", hackathonId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)));
    }

    @Test
    void getWinnerCandidates_SottomissioniNonValutate_Restituisce400() throws Exception {
        // Arrange
        when(hackathonManager.getHackathon(hackathonId)).thenReturn(hackathon);
        when(winningManager.ottieniTeamConPunteggioMassimo(hackathon))
                .thenThrow(new IllegalArgumentException("L'hackathon in questione ha sottomissioni non valutate"));

        // Act & Assert
        mockMvc.perform(get("/api/hackathons/{hackathonId}/winner-candidates", hackathonId))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", containsString("sottomissioni non valutate")));
    }

    @Test
    void getWinnerCandidates_HackathonNonTrovato_Restituisce404() throws Exception {
        // Arrange
        UUID unknownId = UUID.randomUUID();
        when(hackathonManager.getHackathon(unknownId))
                .thenThrow(new NoSuchElementException("Nessun hackathon trovato con ID: " + unknownId));

        // Act & Assert
        mockMvc.perform(get("/api/hackathons/{hackathonId}/winner-candidates", unknownId))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message", containsString("Nessun hackathon trovato")));
    }

    @Test
    void assegnaVincitore_Valido_Restituisce200() throws Exception {
        // Arrange
        AssegnaVincitoreRequest request = new AssegnaVincitoreRequest("Team Alpha");

        when(hackathonManager.getHackathon(hackathonId)).thenReturn(hackathon);
        when(teamManager.getTeam("Team Alpha")).thenReturn(team1);
        doNothing().when(winningManager).assegnaVincitore(hackathon, team1);

        // Act & Assert
        mockMvc.perform(post("/api/hackathons/{hackathonId}/winner", hackathonId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message", is("Vincitore assegnato con successo")));

        verify(winningManager).assegnaVincitore(hackathon, team1);
    }

    @Test
    void assegnaVincitore_SottomissioniNonValutate_Restituisce400() throws Exception {
        // Arrange
        AssegnaVincitoreRequest request = new AssegnaVincitoreRequest("Team Alpha");

        when(hackathonManager.getHackathon(hackathonId)).thenReturn(hackathon);
        when(teamManager.getTeam("Team Alpha")).thenReturn(team1);
        doThrow(new IllegalArgumentException("Ci sono sottomissioni non ancora valutate"))
                .when(winningManager).assegnaVincitore(hackathon, team1);

        // Act & Assert
        mockMvc.perform(post("/api/hackathons/{hackathonId}/winner", hackathonId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", containsString("non ancora valutate")));
    }

    @Test
    void assegnaVincitore_TeamNonTrovato_Restituisce400() throws Exception {
        // Arrange
        AssegnaVincitoreRequest request = new AssegnaVincitoreRequest("Team Inesistente");

        when(hackathonManager.getHackathon(hackathonId)).thenReturn(hackathon);
        when(teamManager.getTeam("Team Inesistente"))
                .thenThrow(new NoSuchElementException("Nessun team trovato con nome: Team Inesistente"));

        // Act & Assert
        mockMvc.perform(post("/api/hackathons/{hackathonId}/winner", hackathonId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void assegnaVincitore_BodyVuoto_Restituisce400() throws Exception {
        // Act & Assert
        mockMvc.perform(post("/api/hackathons/{hackathonId}/winner", hackathonId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isBadRequest());
    }

    /**
     * Crea un'iscrizione con valutazione.
     */
    private Iscrizione createIscrizioneValutata(Team team, int voto) {
        Sottomissione sottomissione = new Sottomissione("Sottomissione " + team.getNome(), "Descrizione", new File("."));
        sottomissione.setValutazione(new Valutazione(voto, "Ottimo lavoro"));
        Iscrizione iscrizione = new Iscrizione();
        iscrizione.setSottomissione(sottomissione);
        return iscrizione;
    }
}
