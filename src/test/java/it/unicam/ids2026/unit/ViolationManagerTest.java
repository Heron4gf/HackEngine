package it.unicam.ids2026.unit;

import it.unicam.ids2026.core.hackathon.Hackathon;
import it.unicam.ids2026.core.hackathon.data.DatiHackathon;
import it.unicam.ids2026.core.hackathon.data.Intervallo;
import it.unicam.ids2026.core.managers.ViolationManager;
import it.unicam.ids2026.core.roles.staff.Giudice;
import it.unicam.ids2026.core.roles.staff.Mentore;
import it.unicam.ids2026.core.roles.staff.Organizzatore;
import it.unicam.ids2026.core.roles.team.Iscrizione;
import it.unicam.ids2026.core.roles.team.Team;
import it.unicam.ids2026.core.transaction.MoneyAmount;
import it.unicam.ids2026.core.violation.Violazione;
import it.unicam.ids2026.persistence.HackathonRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Currency;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class ViolationManagerTest {

    @Mock
    private HackathonRepository hackathonRepository;

    private ViolationManager violationManager;
    private Hackathon hackathon;
    private Mentore mentore;
    private Team team;

    @BeforeEach
    void setUp() {
        violationManager = new ViolationManager(hackathonRepository);
        hackathon = createHackathon();
        mentore = new Mentore("Mario", "Rossi");
        team = new Team("Team Test", 5);
        hackathon.getIscritti().put(team, new Iscrizione());
    }

    @Test
    void segnalaTeam_ShouldApplyDomainValidationOnce() {
        Violazione violazione = violationManager.segnalaTeam(
                hackathon,
                team,
                mentore,
                "Uso improprio del repository"
        );

        assertSame(team, violazione.getColpevole());
        assertSame(mentore, violazione.getSegnalatoDa());
        assertTrue(hackathon.getViolazioni().contains(violazione));
        verify(hackathonRepository).save(hackathon);
    }

    private Hackathon createHackathon() {
        LocalDateTime now = LocalDateTime.now();
        DatiHackathon dati = new DatiHackathon(
                "Hackathon Test",
                "Roma",
                new MoneyAmount(new BigDecimal("1000.00"), Currency.getInstance("EUR")),
                5,
                "Regolamento test"
        );
        return new Hackathon(
                new Organizzatore("Laura", "Bianchi"),
                dati,
                new Giudice("Luigi", "Verdi"),
                new Intervallo(now.plusDays(1), now.plusDays(10)),
                new Intervallo(now.plusDays(15), now.plusDays(17))
        );
    }
}
