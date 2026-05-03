package it.unicam.ids2026.unit;

import it.unicam.ids2026.api.external.calendar.ICalendar;
import it.unicam.ids2026.core.hackathon.Hackathon;
import it.unicam.ids2026.core.hackathon.data.DatiHackathon;
import it.unicam.ids2026.core.hackathon.data.Intervallo;
import it.unicam.ids2026.core.hackathon.status.StatoInCorso;
import it.unicam.ids2026.core.managers.SupportRequestManager;
import it.unicam.ids2026.core.roles.staff.Giudice;
import it.unicam.ids2026.core.roles.staff.Organizzatore;
import it.unicam.ids2026.core.roles.team.Iscrizione;
import it.unicam.ids2026.core.roles.team.Team;
import it.unicam.ids2026.core.supportRequest.RichiestaSupporto;
import it.unicam.ids2026.core.transaction.MoneyAmount;
import it.unicam.ids2026.persistence.HackathonRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Currency;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SupportRequestManagerTest {

    @Mock
    private ICalendar calendarService;
    @Mock
    private HackathonRepository hackathonRepository;

    private SupportRequestManager supportRequestManager;
    private Hackathon hackathon;
    private Team team;

    @BeforeEach
    void setUp() {
        supportRequestManager = new SupportRequestManager(calendarService, hackathonRepository);
        hackathon = createHackathon();
        hackathon.setState(new StatoInCorso());
        team = new Team("Team Test", 5);
        hackathon.getIscritti().put(team, new Iscrizione());
    }

    @Test
    void creaRichiestaSupporto_ByIds_ShouldUseCanonicalHackathonMethod() {
        when(hackathonRepository.findById(hackathon.getId())).thenReturn(Optional.of(hackathon));

        RichiestaSupporto richiesta = supportRequestManager.creaRichiestaSupporto(
                "Build bloccata",
                "La pipeline fallisce in test",
                hackathon.getId(),
                team.getId()
        );

        assertSame(richiesta, hackathon.getIscritti().get(team).getRichiestaSupporto());
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
