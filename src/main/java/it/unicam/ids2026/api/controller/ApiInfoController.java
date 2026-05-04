package it.unicam.ids2026.api.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class ApiInfoController {

    @GetMapping
    public ResponseEntity<Map<String, List<String>>> getApiIndex() {
        return ResponseEntity.ok(Map.of(
                "hackathons", List.of(
                        "GET /api/hackathons",
                        "POST /api/hackathons",
                        "GET /api/hackathons/{id}"
                ),
                "teams", List.of(
                        "POST /api/teams",
                        "GET /api/teams/{nome}",
                        "POST /api/teams/{nome}/iscrizione"
                ),
                "users", List.of(
                        "GET /api/users",
                        "POST /api/users",
                        "GET /api/users/{id}"
                ),
                "invites", List.of(
                        "POST /api/invites",
                        "GET /api/invites/casella/{utenteId}"
                )
        ));
    }
}
