package it.unicam.ids2026.unit;

import it.unicam.ids2026.core.hackathon.Hackathon;
import it.unicam.ids2026.core.hackathon.data.DatiHackathon;
import it.unicam.ids2026.core.hackathon.data.Intervallo;
import it.unicam.ids2026.core.managers.TeamManager;
import it.unicam.ids2026.core.managers.ViolationManager;
import it.unicam.ids2026.core.roles.staff.Giudice;
import it.unicam.ids2026.core.roles.staff.Mentore;
import it.unicam.ids2026.core.roles.staff.Organizzatore;
import it.unicam.ids2026.core.roles.team.Iscrizione;
import it.unicam.ids2026.core.roles.team.Team;
import it.unicam.ids2026.core.transaction.MoneyAmount;
import it.unicam.ids2026.core.violation.Violazione;
import it.unicam.ids2026.persistence.HackathonRepository;
import it.unicam.ids2026.persistence.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Currency;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ViolationManagerTest {

    @Mock
    private HackathonRepository hackathonRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private TeamManager teamManager;

    private ViolationManager violationManager;
    private Hackathon hackathon;
    private Mentore mentore;
    private Team team;

    @BeforeEach
    void setUp() {
        violationManager = new ViolationManager(hackathonRepository, userRepository, teamManager);
        hackathon = createHackathon();
        mentore = new Mentore("Mario", "Rossi");
        team = new Team("Team Test", 5);
        hackathon.getIscritti().put(team, new Iscrizione());
    }

    @Test
    void segnalaTeam_ByIds_ShouldResolveEntitiesAndApplyDomainValidationOnce() {
        UUID hackathonId = hackathon.getId();
        UUID mentoreId = mentore.getId();
        UUID teamId = team.getId();

        when(hackathonRepository.findById(hackathonId)).thenReturn(Optional.of(hackathon));
        when(userRepository.findById(mentoreId)).thenReturn(Optional.of(mentore));
        when(teamManager.getTeam(teamId)).thenReturn(team);

        Violazione violazione = violationManager.segnalaTeam(
                hackathonId,
                mentoreId,
                teamId,
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
