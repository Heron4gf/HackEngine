package it.unicam.ids2026.slice;

import it.unicam.ids2026.api.controller.HackathonController;
import it.unicam.ids2026.core.hackathon.Hackathon;
import it.unicam.ids2026.core.hackathon.data.DatiHackathon;
import it.unicam.ids2026.core.hackathon.data.Intervallo;
import it.unicam.ids2026.core.hackathon.wallet.HackathonWallet;
import it.unicam.ids2026.core.managers.HackathonManager;
import it.unicam.ids2026.core.managers.StaffManager;
import it.unicam.ids2026.core.managers.UserManager;
import it.unicam.ids2026.core.roles.staff.Giudice;
import it.unicam.ids2026.core.roles.staff.Organizzatore;
import it.unicam.ids2026.core.transaction.MoneyAmount;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(HackathonController.class)
class HackathonControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private HackathonManager hackathonManager;

    @MockitoBean
    private StaffManager staffManager;

    @MockitoBean
    private UserManager userManager;

    @Test
    void createHackathon_ValidBody_ShouldReturn201WithIdAndNome() throws Exception {
        // Arrange
        UUID organizzatoreId = UUID.randomUUID();
        UUID giudiceId = UUID.randomUUID();
        UUID hackathonId = UUID.randomUUID();

        Organizzatore organizzatore = new Organizzatore("Mario", "Rossi");
        Giudice giudice = new Giudice("Luigi", "Verdi");

        when(userManager.getUserById(organizzatoreId)).thenReturn(organizzatore);
        when(userManager.getUserById(giudiceId)).thenReturn(giudice);

        LocalDateTime now = LocalDateTime.now();
        Hackathon hackathon = new Hackathon(
                hackathonId,
                organizzatore,
                new DatiHackathon(
                        "Hackathon Test",
                        "Roma",
                        new MoneyAmount(
                                new BigDecimal("1000.00"),
                                Currency.getInstance("EUR")
                        ),
                        5,
                        "Regolamento"),
                giudice,
                new Intervallo(now.plusDays(1), now.plusDays(10)),
                new Intervallo(now.plusDays(15), now.plusDays(17)),
                Collections.emptySet(),
                Collections.emptyMap(),
                new HashSet<>(),
                new HackathonWallet(Currency.getInstance("EUR"))
        );

        when(hackathonManager.creaHackathon(any(), any(), any(), any(), any())).thenReturn(hackathon);

        String requestBody = String.format("""
            {
                "organizzatoreId": "%s",
                "nome": "Hackathon Test",
                "luogo": "Roma",
                "premioInDenaro": 1000.00,
                "currency": "EUR",
                "dimensioneMaxTeam": 5,
                "regolamento": "Regolamento",
                "iscrizioni": {
                    "dataInizio": "%s",
                    "dataFine": "%s"
                },
                "durata": {
                    "dataInizio": "%s",
                    "dataFine": "%s"
                },
                "giudiceId": "%s"
            }
            """,
            organizzatoreId,
            now.plusDays(1).toString(),
            now.plusDays(10).toString(),
            now.plusDays(15).toString(),
            now.plusDays(17).toString(),
            giudiceId
        );

        // Act & Assert
        mockMvc.perform(post("/api/hackathons")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.id", is(hackathonId.toString())))
            .andExpect(jsonPath("$.nome", is("Hackathon Test")));
    }

    @Test
    void createHackathon_MissingNome_ShouldReturn400WithFieldName() throws Exception {
        // Arrange
        UUID organizzatoreId = UUID.randomUUID();
        UUID giudiceId = UUID.randomUUID();
        LocalDateTime now = LocalDateTime.now();

        String requestBody = String.format("""
            {
                "organizzatoreId": "%s",
                "nome": "",
                "luogo": "Roma",
                "premioInDenaro": 1000.00,
                "currency": "EUR",
                "dimensioneMaxTeam": 5,
                "regolamento": "Regolamento",
                "iscrizioni": {
                    "dataInizio": "%s",
                    "dataFine": "%s"
                },
                "durata": {
                    "dataInizio": "%s",
                    "dataFine": "%s"
                },
                "giudiceId": "%s"
            }
            """,
            organizzatoreId,
            now.plusDays(1).toString(),
            now.plusDays(10).toString(),
            now.plusDays(15).toString(),
            now.plusDays(17).toString(),
            giudiceId
        );

        // Act & Assert
        mockMvc.perform(post("/api/hackathons")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.message", containsString("nome")));
    }

    @Test
    void createHackathon_InvalidDates_ShouldReturn400() throws Exception {
        // Arrange
        UUID organizzatoreId = UUID.randomUUID();
        UUID giudiceId = UUID.randomUUID();
        LocalDateTime now = LocalDateTime.now();

        when(userManager.getUserById(eq(organizzatoreId))).thenReturn(new Organizzatore("Mario", "Rossi"));
        when(userManager.getUserById(eq(giudiceId))).thenReturn(new Giudice("Luigi", "Verdi"));

        when(hackathonManager.creaHackathon(any(), any(), any(), any(), any()))
            .thenThrow(new IllegalArgumentException("Range date inizio o durata invalide"));

        String requestBody = String.format("""
            {
                "organizzatoreId": "%s",
                "nome": "Hackathon Test",
                "luogo": "Roma",
                "premioInDenaro": 1000.00,
                "currency": "EUR",
                "dimensioneMaxTeam": 5,
                "regolamento": "Regolamento",
                "iscrizioni": {
                    "dataInizio": "%s",
                    "dataFine": "%s"
                },
                "durata": {
                    "dataInizio": "%s",
                    "dataFine": "%s"
                },
                "giudiceId": "%s"
            }
            """,
            organizzatoreId,
            now.plusDays(20).toString(),
            now.plusDays(25).toString(),
            now.plusDays(15).toString(),
            now.plusDays(17).toString(),
            giudiceId
        );

        // Act & Assert
        mockMvc.perform(post("/api/hackathons")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.message", containsString("Range date")));
    }

    @Test
    void getAllHackathons_ShouldReturn200WithList() throws Exception {
        // Arrange
        when(hackathonManager.getHackathons()).thenReturn(Set.of());

        // Act & Assert
        mockMvc.perform(get("/api/hackathons"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    void getHackathon_ValidUuid_ShouldReturn200() throws Exception {
        // Arrange
        UUID hackathonId = UUID.randomUUID();
        Organizzatore organizzatore = new Organizzatore("Mario", "Rossi");
        Giudice giudice = new Giudice("Luigi", "Verdi");
        LocalDateTime now = LocalDateTime.now();

        Hackathon hackathon = new Hackathon(
                hackathonId,
                organizzatore,
                new DatiHackathon("Test", "Roma", new MoneyAmount(new BigDecimal("1000"), Currency.getInstance("EUR")), 5, "Reg"),
                giudice,
                new Intervallo(now.plusDays(1), now.plusDays(10)),
                new Intervallo(now.plusDays(15), now.plusDays(17)),
                Collections.emptySet(),
                Collections.emptyMap(),
                new HashSet<>(),
                new HackathonWallet(Currency.getInstance("EUR"))
        );

        when(hackathonManager.getHackathon(hackathonId)).thenReturn(hackathon);

        // Act & Assert
        mockMvc.perform(get("/api/hackathons/{id}", hackathonId))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id", is(hackathonId.toString())));
    }

    @Test
    void getHackathon_UnknownUuid_ShouldReturn404() throws Exception {
        // Arrange
        UUID unknownId = UUID.randomUUID();
        when(hackathonManager.getHackathon(unknownId))
            .thenThrow(new NoSuchElementException("Nessun hackathon trovato con ID: " + unknownId));

        // Act & Assert
        mockMvc.perform(get("/api/hackathons/{id}", unknownId))
            .andExpect(status().isNotFound())
            .andExpect(jsonPath("$.message", containsString("Nessun hackathon trovato")));
    }

    @Test
    void getJoinableHackathons_ShouldReturn200() throws Exception {
        // Arrange
        when(hackathonManager.getJoinableHackathons()).thenReturn(Set.of());

        // Act & Assert
        mockMvc.perform(get("/api/hackathons/joinable"))
            .andExpect(status().isOk());
    }

    @Test
    void chiudiSottomissioni_ShouldReturn200() throws Exception {
        // Arrange
        UUID hackathonId = UUID.randomUUID();
        doNothing().when(hackathonManager).chiudiSottomissioni(hackathonId);

        // Act & Assert
        mockMvc.perform(post("/api/hackathons/{id}/chiudi-sottomissioni", hackathonId))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.message", is("Sottomissioni chiuse con successo")));
    }

    @Test
    void avanzaStato_ShouldReturn200() throws Exception {
        // Arrange
        UUID hackathonId = UUID.randomUUID();
        Organizzatore organizzatore = new Organizzatore("Mario", "Rossi");
        Giudice giudice = new Giudice("Luigi", "Verdi");
        LocalDateTime now = LocalDateTime.now();

        Hackathon hackathon = new Hackathon(
                hackathonId,
                organizzatore,
                new DatiHackathon("Test", "Roma", new MoneyAmount(new BigDecimal("1000"), Currency.getInstance("EUR")), 5, "Reg"),
                giudice,
                new Intervallo(now.plusDays(1), now.plusDays(10)),
                new Intervallo(now.plusDays(15), now.plusDays(17)),
                Collections.emptySet(),
                Collections.emptyMap(),
                new HashSet<>(),
                new HackathonWallet(Currency.getInstance("EUR"))
        );

        when(hackathonManager.getHackathon(hackathonId)).thenReturn(hackathon);
        doNothing().when(hackathonManager).avanzaStato(hackathon);

        // Act & Assert
        mockMvc.perform(post("/api/hackathons/{id}/avanza-stato", hackathonId))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.message", is("Stato avanzato con successo")));
    }
}
