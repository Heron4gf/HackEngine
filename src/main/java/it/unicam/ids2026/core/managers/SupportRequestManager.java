package it.unicam.ids2026.core.managers;

import it.unicam.ids2026.api.external.DefaultCalendarWrapper;
import it.unicam.ids2026.api.external.ICalendar;
import it.unicam.ids2026.core.hackathon.Hackathon;
import it.unicam.ids2026.core.hackathon.data.Disponibilita;
import it.unicam.ids2026.core.roles.User;
import it.unicam.ids2026.core.roles.team.Team;
import it.unicam.ids2026.core.roles.team.Utente;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
public class SupportRequestManager {

    private ICalendar calendarService = new DefaultCalendarWrapper();

    public void creaRichiestaSupporto(Hackathon hackathon, Team team, String titolo, String descrizione) {

    }

    public void registraDisponibilita(Utente utente, Disponibilita disponibilita) {

    }

    /*public Set<RichiestaSuporto> visualizzaRichieste(Hackathon hackathon) {

    }*/

    /*public void rispondiTestualmente(RichiestaSupporto richiestaSupporto, String messaggio) {

    }

    public void fissaCall(RichiestaSupporto richiestaSupporto) {

    }*/

    private void fissaImpegno(@NonNull User user, @NonNull LocalDateTime dateTime) {
        calendarService.fissaImpegno(user, dateTime);
    }

    private boolean validaDati(Hackathon hackathon, Team team, String titolo, String descrizione) {
        return true;
    }

    private boolean validaDisponibilita(Utente utente, Disponibilita disponibilita) {
        return true;
    }

}
