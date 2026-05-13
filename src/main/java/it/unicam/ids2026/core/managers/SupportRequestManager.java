package it.unicam.ids2026.core.managers;

import it.unicam.ids2026.api.external.calendar.ICalendar;
import it.unicam.ids2026.core.hackathon.Hackathon;
import it.unicam.ids2026.core.hackathon.data.Disponibilita;
import it.unicam.ids2026.core.roles.User;
import it.unicam.ids2026.core.roles.staff.Mentore;
import it.unicam.ids2026.core.roles.team.Iscrizione;
import it.unicam.ids2026.core.roles.team.Team;
import it.unicam.ids2026.core.roles.team.Utente;
import it.unicam.ids2026.core.supportRequest.RichiestaSupporto;
import it.unicam.ids2026.core.supportRequest.StatoRichiesta;
import it.unicam.ids2026.core.supportRequest.response.RispostaCall;
import it.unicam.ids2026.core.supportRequest.response.RispostaTestuale;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SupportRequestManager {

    private final ICalendar calendarService;

    /**
     * Crea una nuova richiesta di supporto per il {@link Team} specificato
     * all'interno dell'hackathon indicato.
     *
     * <p>I dati forniti (titolo, descrizione, team e hackathon) vengono
     * validati tramite il metodo {@code validaDati}. Se la validazione fallisce,
     * viene sollevata una {@link IllegalArgumentException}.</p>
     *
     * <p>In caso di dati validi, viene creata una nuova istanza di
     * {@link RichiestaSupporto} e associata al team tramite
     * {@code hackathon.aggiungiRichiestaSupporto}. La richiesta appena creata
     * viene quindi restituita al chiamante.</p>
     *
     * @param hackathon   l'hackathon a cui appartiene il team; non deve essere {@code null}
     * @param team        il team che effettua la richiesta di supporto; non deve essere {@code null}
     * @param titolo      il titolo della richiesta di supporto; non deve essere {@code null}
     * @param descrizione la descrizione dettagliata della richiesta; non deve essere {@code null}
     * @return la richiesta di supporto appena creata
     * @throws IllegalArgumentException se i dati forniti non superano la validazione
     */
    public RichiestaSupporto creaRichiestaSupporto(@NonNull Hackathon hackathon,
                                                   @NonNull Team team,
                                                   @NonNull String titolo,
                                                   @NonNull String descrizione) {
        if (!validaDati(hackathon, team, titolo, descrizione)) {
            throw new IllegalArgumentException("Dati invalidi");
        }

        RichiestaSupporto richiestaSupporto = new RichiestaSupporto(titolo, descrizione);
        hackathon.aggiungiRichiestaSupporto(team, richiestaSupporto);
        return richiestaSupporto;
    }

    /**
     * Restituisce l'insieme delle richieste di supporto presenti
     * tra gli iscritti dell'hackathon specificato.
     *
     * <p>Per ogni {@link Iscrizione} viene estratta la relativa
     * {@link RichiestaSupporto}. Le richieste nulle vengono ignorate,
     * così da restituire solo quelle effettivamente presenti.</p>
     *
     * @param hackathon l'hackathon da cui estrarre le richieste di supporto; non deve essere {@code null}
     * @return un insieme contenente tutte le richieste di supporto non nulle
     */
    public Set<RichiestaSupporto> visualizzaRichieste(@NonNull Hackathon hackathon) {
        return hackathon.getIscritti().values().stream()
                .map(Iscrizione::getRichiestaSupporto)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());
    }

    public RichiestaSupporto ottieniRichiesta(@NonNull Hackathon hackathon, @NonNull Team team) {
        return hackathon.getIscritti().get(team).getRichiestaSupporto();
    }


    /**
     * Restituisce la disponibilità dell'utente per l'hackathon specificato.
     *
     * <p>La disponibilità viene ottenuta tramite il {@code calendarService},
     * utilizzando la durata dell'hackathon come intervallo di riferimento.
     * Il metodo non esegue ulteriori validazioni: eventuali errori o assenze
     * di disponibilità dipendono dalla logica interna del servizio.</p>
     *
     * @param utente    l'utente di cui recuperare il calendario; non deve essere {@code null}
     * @param hackathon l'hackathon per cui calcolare la disponibilità; non deve essere {@code null}
     * @return la disponibilità dell'utente per la durata dell'hackathon
     */
    public Disponibilita ottieniCalendario(@NonNull User utente, @NonNull Hackathon hackathon) {
        return calendarService.getDisponibilita(utente, hackathon.getDurataHackathon());
    }

    /**
     * Registra la disponibilità dell'utente per l'hackathon specificato.
     *
     * <p>La disponibilità fornita viene validata tramite il metodo
     * {@code validaDisponibilita}. Se la validazione ha esito positivo,
     * la disponibilità viene assegnata all'iscrizione del team
     * dell'utente all'interno dell'hackathon. Se la validazione fallisce,
     * viene sollevata una {@link IllegalArgumentException}.</p>
     *
     * @param hackathon     l'hackathon per cui registrare la disponibilità; non deve essere {@code null}
     * @param utente        l'utente che sta registrando la disponibilità; non deve essere {@code null}
     * @param disponibilita la disponibilità da registrare; non deve essere {@code null}
     * @throws IllegalArgumentException se la disponibilità non supera la validazione
     */
    public void registraDisponibilita(@NonNull Hackathon hackathon,
                                      @NonNull Utente utente,
                                      @NonNull Disponibilita disponibilita) {
        if (validaDisponibilita(hackathon, utente, disponibilita)) {
            hackathon.getIscritti().get(utente.getTeam()).setDisponibilita(disponibilita);
        } else throw new IllegalArgumentException("Disponibilità invalida");
    }

    /**
     * Restituisce la disponibilità del team all'interno dell'hackathon specificato.
     *
     * <p>La disponibilità viene recuperata dall'iscrizione del team all'hackathon.
     * Se il team non risulta iscritto, viene sollevata una
     * {@link IllegalArgumentException}.</p>
     *
     * @param hackathon l'hackathon di riferimento; non deve essere {@code null}
     * @param team      il team di cui recuperare la disponibilità; non deve essere {@code null}
     * @return la disponibilità associata al team
     * @throws IllegalArgumentException se il team non è iscritto all'hackathon
     */
    public Disponibilita ottieniDisponibilita(@NonNull Hackathon hackathon, @NonNull Team team) {
        Iscrizione iscrizione = hackathon.getIscritti().get(team);
        if (iscrizione == null) throw new IllegalArgumentException("Il team non è iscritto all'hackathon");
        return iscrizione.getDisponibilita();
    }


    /**
     * Imposta una risposta testuale per la richiesta di supporto specificata.
     *
     * <p>La risposta viene incapsulata in una nuova istanza di
     * {@link RispostaTestuale} e assegnata alla richiesta tramite
     * {@code richiestaSupporto.setRisposta}.</p>
     *
     * @param richiestaSupporto la richiesta di supporto a cui rispondere; non deve essere {@code null}
     * @param messaggio         il contenuto testuale della risposta; non deve essere {@code null}
     */
    public void rispondiTestualmente(@NonNull RichiestaSupporto richiestaSupporto, @NonNull Mentore incaricato, @NonNull String messaggio) {
        richiestaSupporto.setRisposta(new RispostaTestuale(messaggio));
        richiestaSupporto.setIncaricato(incaricato);
        richiestaSupporto.setStato(StatoRichiesta.CHIUSA);
    }


    /**
     * Imposta una risposta di tipo call per la richiesta di supporto specificata.
     *
     * <p>La risposta viene rappresentata tramite una nuova istanza di
     * {@link RispostaCall}, che contiene la data e ora della call da fissare.</p>
     *
     * @param richiestaSupporto la richiesta di supporto a cui associare la call; non deve essere {@code null}
     * @param dateTime          la data e ora della call; non deve essere {@code null}
     */
    public void fissaCall(@NonNull RichiestaSupporto richiestaSupporto, @NonNull Mentore incaricato, @NonNull LocalDateTime dateTime) {
        richiestaSupporto.setRisposta(new RispostaCall(dateTime));
        richiestaSupporto.setIncaricato(incaricato);
        richiestaSupporto.setStato(StatoRichiesta.PRESA_IN_CARICO);
    }


    private void fissaImpegno(User user, LocalDateTime dateTime) {
        calendarService.fissaImpegno(user, dateTime);
    }

    private boolean validaDati(Hackathon hackathon, Team team, String titolo, String descrizione) {
        return hackathon.getIscritti().containsKey(team)
                && !titolo.isBlank()
                && !descrizione.isBlank();
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
