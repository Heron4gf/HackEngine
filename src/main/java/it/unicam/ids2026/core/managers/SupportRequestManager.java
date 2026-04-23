package it.unicam.ids2026.core.managers;

import it.unicam.ids2026.api.external.DefaultCalendarWrapper;
import it.unicam.ids2026.api.external.ICalendar;
import it.unicam.ids2026.core.hackathon.Hackathon;
import it.unicam.ids2026.core.hackathon.data.Disponibilita;
import it.unicam.ids2026.core.roles.User;
import it.unicam.ids2026.core.roles.team.Iscrizione;
import it.unicam.ids2026.core.roles.team.Team;
import it.unicam.ids2026.core.roles.team.Utente;
import it.unicam.ids2026.core.supportRequest.RichiestaSupporto;
import it.unicam.ids2026.core.supportRequest.response.RispostaCall;
import it.unicam.ids2026.core.supportRequest.response.RispostaTestuale;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@AllArgsConstructor
public class SupportRequestManager {

    private ICalendar calendarService;

    public void creaRichiestaSupporto(@NonNull Hackathon hackathon, @NonNull Team team, @NonNull String titolo, @NonNull String descrizione) {
        if(validaDati(hackathon, team, titolo, descrizione)) {
            RichiestaSupporto richiestaSupporto = new RichiestaSupporto(titolo, descrizione);
            hackathon.aggiungiRichiestaSupporto(team, richiestaSupporto);
        } else throw new IllegalArgumentException("Dati invalidi");
    }

    public void registraDisponibilita(@NonNull Hackathon hackathon, @NonNull Utente utente, @NonNull Disponibilita disponibilita) {
        if(validaDisponibilita(hackathon, utente, disponibilita)) {
            hackathon.getIscritti().get(utente.getTeam()).setDisponibilita(disponibilita);
        } else throw new IllegalArgumentException("Disponibilità invalida");
    }

    public Set<RichiestaSupporto> visualizzaRichieste(@NonNull Hackathon hackathon) {
        Set<RichiestaSupporto> ret = new HashSet<>();
        for(Iscrizione iscrizione : hackathon.getIscritti().values()) {
            ret.add(iscrizione.getRichiestaSupporto());
        }
        return ret;
    }

    public void rispondiTestualmente(@NonNull RichiestaSupporto richiestaSupporto, @NonNull String messaggio) {
        richiestaSupporto.setRisposta(new RispostaTestuale(messaggio));
    }

    public void fissaCall(@NonNull RichiestaSupporto richiestaSupporto, @NonNull LocalDateTime dateTime) {
        richiestaSupporto.setRisposta(new RispostaCall(dateTime));
    }

    private void fissaImpegno(User user, LocalDateTime dateTime) {
        calendarService.fissaImpegno(user, dateTime);
    }

    private boolean validaDati(Hackathon hackathon, Team team, String titolo, String descrizione) {
        return hackathon.getIscritti().containsKey(team)
                && !titolo.isEmpty()
                && !descrizione.isEmpty();
    }

    private boolean validaDisponibilita(Hackathon hackathon, Utente utente, Disponibilita disponibilita) {
        Disponibilita real = calendarService.getDisponibilita(utente, hackathon.getDurataHackathon());

        return disponibilita.getDisponibilita().stream().allMatch(p ->
                real.getDisponibilita().stream().anyMatch(r ->
                        !p.dataInizio().isBefore(r.dataInizio()) && !p.dataFine().isAfter(r.dataFine())
                )
        );
    }

}
