package it.unicam.ids2026.integration;

import it.unicam.ids2026.api.dto.request.CreateUserRequest;
import it.unicam.ids2026.api.dto.response.HackathonResponse;
import it.unicam.ids2026.api.dto.response.MessageResponse;
import it.unicam.ids2026.api.dto.response.TeamResponse;
import it.unicam.ids2026.api.dto.response.UserResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.client.ResponseErrorHandler;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Currency;
import java.util.Set;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class HackathonIntegrationTest {

    @LocalServerPort
    private int port;

    private RestTemplate restTemplate;

    @BeforeEach
    void setUp() {
        restTemplate = new RestTemplate();
        restTemplate.setErrorHandler(new ResponseErrorHandler() {
            @Override
            public boolean hasError(ClientHttpResponse response) throws IOException {
                return false;
            }
            public void handleError(ClientHttpResponse response) throws IOException {
            }
        });
    }

    private String baseUrl() {
        return "http://localhost:" + port + "/api";
    }

    private HttpEntity<String> jsonRequest(String json) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        return new HttpEntity<>(json, headers);
    }

    @Test
    void contextLoads() {
        assertTrue(port > 0);
    }

    @Test
    void fullLifecycle_HappyPath_ShouldCompleteSuccessfully() {
        // Step 1: Create Organizzatore
        ResponseEntity<UserResponse> organizzatoreResponse = restTemplate.postForEntity(
                baseUrl() + "/users",
                new CreateUserRequest(CreateUserRequest.UserRole.ORGANIZZATORE, "Mario", "Rossi"),
                UserResponse.class
        );
        assertEquals(HttpStatus.CREATED, organizzatoreResponse.getStatusCode());
        UUID organizzatoreId = organizzatoreResponse.getBody().id();

        // Step 2: Create Giudice
        ResponseEntity<UserResponse> giudiceResponse = restTemplate.postForEntity(
                baseUrl() + "/users",
                new CreateUserRequest(CreateUserRequest.UserRole.GIUDICE, "Luigi", "Verdi"),
                UserResponse.class
        );
        assertEquals(HttpStatus.CREATED, giudiceResponse.getStatusCode());
        UUID giudiceId = giudiceResponse.getBody().id();

        // Step 3: Create Hackathon
        LocalDateTime now = LocalDateTime.now();
        String hackathonRequest = String.format("""
            {
                "organizzatoreId": "%s",
                "nome": "Hackathon Integration Test",
                "luogo": "Roma",
                "premioInDenaro": 5000.00,
                "currency": "EUR",
                "dimensioneMaxTeam": 4,
                "regolamento": "Regolamento di test",
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

        ResponseEntity<HackathonResponse> hackathonResponse = restTemplate.postForEntity(
                baseUrl() + "/hackathons",
                jsonRequest(hackathonRequest),
                HackathonResponse.class
        );
        assertEquals(HttpStatus.CREATED, hackathonResponse.getStatusCode());
        UUID hackathonId = hackathonResponse.getBody().id();
        assertEquals("Hackathon Integration Test", hackathonResponse.getBody().nome());

        // Step 4: Get Hackathon and verify data
        ResponseEntity<HackathonResponse> getHackathonResponse = restTemplate.getForEntity(
                baseUrl() + "/hackathons/" + hackathonId,
                HackathonResponse.class
        );
        assertEquals(HttpStatus.OK, getHackathonResponse.getStatusCode());
        assertEquals("Hackathon Integration Test", getHackathonResponse.getBody().nome());
        assertEquals(organizzatoreId, getHackathonResponse.getBody().organizzatoreId());

        // Step 5: Get joinable hackathons - should include our hackathon (in ISCRIZIONE state)
        ResponseEntity<HackathonResponse[]> joinableResponse = restTemplate.getForEntity(
                baseUrl() + "/hackathons/joinable",
                HackathonResponse[].class
        );
        assertEquals(HttpStatus.OK, joinableResponse.getStatusCode());
        assertTrue(Set.of(joinableResponse.getBody()).stream()
                .anyMatch(h -> h.id().equals(hackathonId)));

        // Step 6: Create Utente
        ResponseEntity<UserResponse> utenteResponse = restTemplate.postForEntity(
                baseUrl() + "/users",
                new CreateUserRequest(CreateUserRequest.UserRole.UTENTE, "Paolo", null),
                UserResponse.class
        );
        assertEquals(HttpStatus.CREATED, utenteResponse.getStatusCode());
        UUID utenteId = utenteResponse.getBody().id();

        // Step 7: Create Team
        String teamRequest = String.format("""
            {
                "utenteId": "%s",
                "nome": "Team Integration",
                "maxMembri": 4
            }
            """, utenteId);

        ResponseEntity<TeamResponse> teamResponse = restTemplate.postForEntity(
                baseUrl() + "/teams",
                jsonRequest(teamRequest),
                TeamResponse.class
        );
        assertEquals(HttpStatus.CREATED, teamResponse.getStatusCode());
        String teamName = teamResponse.getBody().nome();

        // Step 8: Register team to hackathon
        ResponseEntity<MessageResponse> iscrizioneResponse = restTemplate.postForEntity(
                baseUrl() + "/teams/" + teamName + "/iscrizione?hackathonId=" + hackathonId + "&utenteId=" + utenteId,
                null,
                MessageResponse.class
        );
        assertEquals(HttpStatus.OK, iscrizioneResponse.getStatusCode());
        assertEquals("Team iscritto all'hackathon", iscrizioneResponse.getBody().message());

        // Step 9: Advance state from ISCRIZIONE to IN_CORSO
        ResponseEntity<MessageResponse> avanzaStatoResponse = restTemplate.postForEntity(
                baseUrl() + "/hackathons/" + hackathonId + "/avanza-stato",
                null,
                MessageResponse.class
        );
        assertEquals(HttpStatus.OK, avanzaStatoResponse.getStatusCode());
        assertEquals("Stato avanzato con successo", avanzaStatoResponse.getBody().message());

        // Step 10: Get joinable hackathons - should NOT include our hackathon anymore
        ResponseEntity<HackathonResponse[]> joinableAfterResponse = restTemplate.getForEntity(
                baseUrl() + "/hackathons/joinable",
                HackathonResponse[].class
        );
        assertEquals(HttpStatus.OK, joinableAfterResponse.getStatusCode());
        assertFalse(Set.of(joinableAfterResponse.getBody()).stream()
                .anyMatch(h -> h.id().equals(hackathonId)));

        // Step 11: Close submissions
        ResponseEntity<MessageResponse> chiudiResponse = restTemplate.postForEntity(
                baseUrl() + "/hackathons/" + hackathonId + "/chiudi-sottomissioni",
                null,
                MessageResponse.class
        );
        assertEquals(HttpStatus.OK, chiudiResponse.getStatusCode());
        assertEquals("Sottomissioni chiuse con successo", chiudiResponse.getBody().message());
    }

    @Test
    void registerTeam_NotInIscrizioneState_ShouldReturn400() {
        // Create Organizzatore and Giudice
        ResponseEntity<UserResponse> organizzatoreResponse = restTemplate.postForEntity(
                baseUrl() + "/users",
                new CreateUserRequest(CreateUserRequest.UserRole.ORGANIZZATORE, "Mario2", "Rossi"),
                UserResponse.class
        );
        UUID organizzatoreId = organizzatoreResponse.getBody().id();

        ResponseEntity<UserResponse> giudiceResponse = restTemplate.postForEntity(
                baseUrl() + "/users",
                new CreateUserRequest(CreateUserRequest.UserRole.GIUDICE, "Luigi2", "Verdi"),
                UserResponse.class
        );
        UUID giudiceId = giudiceResponse.getBody().id();

        // Create Hackathon
        LocalDateTime now = LocalDateTime.now();
        String hackathonRequest = String.format("""
            {
                "organizzatoreId": "%s",
                "nome": "Hackathon Error Test",
                "luogo": "Milano",
                "premioInDenaro": 1000.00,
                "currency": "EUR",
                "dimensioneMaxTeam": 3,
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
                now.minusDays(5).toString(),
                now.minusDays(1).toString(),
                now.plusDays(15).toString(),
                now.plusDays(17).toString(),
                giudiceId
        );

        ResponseEntity<HackathonResponse> hackathonResponse = restTemplate.postForEntity(
                baseUrl() + "/hackathons",
                jsonRequest(hackathonRequest),
                HackathonResponse.class
        );
        UUID hackathonId = hackathonResponse.getBody().id();

        // Advance to IN_CORSO
        restTemplate.postForEntity(
                baseUrl() + "/hackathons/" + hackathonId + "/avanza-stato",
                null,
                MessageResponse.class
        );

        // Create Utente and Team
        ResponseEntity<UserResponse> utenteResponse = restTemplate.postForEntity(
                baseUrl() + "/users",
                new CreateUserRequest(CreateUserRequest.UserRole.UTENTE, "Giovanni", null),
                UserResponse.class
        );
        UUID utenteId = utenteResponse.getBody().id();

        String teamRequest = String.format("""
            {
                "utenteId": "%s",
                "nome": "Team Error",
                "maxMembri": 3
            }
            """, utenteId);

        ResponseEntity<TeamResponse> teamResponse = restTemplate.postForEntity(
                baseUrl() + "/teams",
                jsonRequest(teamRequest),
                TeamResponse.class
        );
        String teamName = teamResponse.getBody().nome();

        // Try to register team when hackathon is not in ISCRIZIONE state
        ResponseEntity<MessageResponse> iscrizioneResponse = restTemplate.postForEntity(
                baseUrl() + "/teams/" + teamName + "/iscrizione?hackathonId=" + hackathonId + "&utenteId=" + utenteId,
                null,
                MessageResponse.class
        );
        assertEquals(HttpStatus.BAD_REQUEST, iscrizioneResponse.getStatusCode());
    }

    @Test
    void createHackathon_InvertedDates_ShouldReturn400() {
        // Create Organizzatore and Giudice
        ResponseEntity<UserResponse> organizzatoreResponse = restTemplate.postForEntity(
                baseUrl() + "/users",
                new CreateUserRequest(CreateUserRequest.UserRole.ORGANIZZATORE, "Mario3", "Rossi"),
                UserResponse.class
        );
        UUID organizzatoreId = organizzatoreResponse.getBody().id();

        ResponseEntity<UserResponse> giudiceResponse = restTemplate.postForEntity(
                baseUrl() + "/users",
                new CreateUserRequest(CreateUserRequest.UserRole.GIUDICE, "Luigi3", "Verdi"),
                UserResponse.class
        );
        UUID giudiceId = giudiceResponse.getBody().id();

        // Create Hackathon with inverted dates (iscrizioni after durata)
        LocalDateTime now = LocalDateTime.now();
        String hackathonRequest = String.format("""
            {
                "organizzatoreId": "%s",
                "nome": "Hackathon Bad Dates",
                "luogo": "Napoli",
                "premioInDenaro": 1000.00,
                "currency": "EUR",
                "dimensioneMaxTeam": 3,
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
                now.plusDays(20).toString(),  // iscrizioni starts after durata
                now.plusDays(25).toString(),
                now.plusDays(15).toString(),
                now.plusDays(17).toString(),
                giudiceId
        );

        ResponseEntity<MessageResponse> response = restTemplate.postForEntity(
                baseUrl() + "/hackathons",
                jsonRequest(hackathonRequest),
                MessageResponse.class
        );
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    void getResource_RandomUuid_ShouldReturn404WithReadableMessage() {
        UUID randomUuid = UUID.randomUUID();

        ResponseEntity<MessageResponse> response = restTemplate.getForEntity(
                baseUrl() + "/hackathons/" + randomUuid,
                MessageResponse.class
        );

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().message().contains("Nessun hackathon trovato"));
    }
}