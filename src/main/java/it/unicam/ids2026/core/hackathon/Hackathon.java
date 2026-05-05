package it.unicam.ids2026.core.hackathon;

import it.unicam.ids2026.core.hackathon.data.DatiHackathon;
import it.unicam.ids2026.core.hackathon.data.Intervallo;
import it.unicam.ids2026.core.hackathon.data.Sottomissione;
import it.unicam.ids2026.core.hackathon.status.RappresentazioneStato;
import it.unicam.ids2026.core.hackathon.status.StatoHackathon;
import it.unicam.ids2026.core.hackathon.status.StatoIscrizione;
import it.unicam.ids2026.core.hackathon.wallet.HackathonWallet;
import it.unicam.ids2026.core.roles.staff.Giudice;
import it.unicam.ids2026.core.roles.staff.Mentore;
import it.unicam.ids2026.core.roles.staff.Organizzatore;
import it.unicam.ids2026.core.roles.team.Iscrizione;
import it.unicam.ids2026.core.roles.team.Team;
import it.unicam.ids2026.core.supportRequest.RichiestaSupporto;
import it.unicam.ids2026.core.transaction.IParteDiPagamento;
import it.unicam.ids2026.core.transaction.Transaction;
import it.unicam.ids2026.core.violation.Violazione;
import lombok.*;

import java.util.*;

@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Hackathon implements IParteDiPagamento {

    @EqualsAndHashCode.Include
    private final UUID id;

    private final Organizzatore organizzatore;
    private final DatiHackathon datiHackathon;
    private final Giudice giudice;
    private final Intervallo periodoIscrizioni;
    private final Intervallo durataHackathon;
    private Team vincitore = null;
    private final Set<Mentore> mentori;

    private final Map<Team, Iscrizione> iscritti;
    private final Set<Violazione> violazioni;
    private final HackathonWallet wallet;
    @Getter(AccessLevel.PRIVATE)
    private StatoHackathon state = new StatoIscrizione();

    /**
     * Full constructor with all fields including wallet.
     */
    @Builder
    public Hackathon(@NonNull UUID id,
                     @NonNull Organizzatore organizzatore,
                     @NonNull DatiHackathon datiHackathon,
                     @NonNull Giudice giudice,
                     @NonNull Intervallo periodoIscrizioni,
                     @NonNull Intervallo durataHackathon,
                     @NonNull Set<Mentore> mentori,
                     @NonNull Map<Team, Iscrizione> iscritti,
                     @NonNull Set<Violazione> violazioni,
                     @NonNull HackathonWallet wallet) {
        this.id = id;
        this.organizzatore = organizzatore;
        this.datiHackathon = datiHackathon;
        this.giudice = giudice;
        this.periodoIscrizioni = periodoIscrizioni;
        this.durataHackathon = durataHackathon;
        this.mentori = mentori;
        this.iscritti = iscritti;
        this.violazioni = violazioni;
        this.wallet = wallet;
    }

    /**
     * Convenience constructor that creates a new wallet with the currency from the prize amount.
     */
    public Hackathon(@NonNull Organizzatore organizzatore,
                     @NonNull DatiHackathon datiHackathon,
                     @NonNull Giudice giudice,
                     @NonNull Intervallo periodoIscrizioni,
                     @NonNull Intervallo durataHackathon) {
        this(
                UUID.randomUUID(),
                organizzatore,
                datiHackathon,
                giudice,
                periodoIscrizioni,
                durataHackathon,
                new HashSet<>(),
                new LinkedHashMap<>(),
                new HashSet<>(),
                new HackathonWallet(datiHackathon.premioInDenaro().getCurrency())
        );
    }

    public void nextState() {
        this.state.next(this);
    }

    public void iscriviTeam(Team team) {
        this.state.iscriviTeam(this, team);
    }

    public void aggiungiMentore(Mentore mentore) {
        this.state.aggiungiMentore(this, mentore);
    }

    public void aggiungiSottomissione(Team team, Sottomissione sottomissione) {
        this.state.aggiungiSottomissione(this, team, sottomissione);
    }

    public Sottomissione getSottomissione(Team team) {
        Iscrizione iscrizione = requireIscrizione(team);
        if (iscrizione.getSottomissione() == null) {
            throw new NoSuchElementException("Nessuna sottomissione trovata per il team specificato");
        }
        return iscrizione.getSottomissione();
    }

    public void aggiungiRichiestaSupporto(Team team, RichiestaSupporto richiestaSupporto) {
        this.state.aggiungiRichiestaSupporto(this, team, richiestaSupporto);
    }

    public Set<Team> getTeams() {
        return Collections.unmodifiableSet(iscritti.keySet());
    }

    public void assegnaVincitore(Team team) {
        this.state.assegnaVincitore(this, team);
    }

    /**
     * Returns the list of all prize transactions.
     *
     * @return unmodifiable view of transactions from the wallet
     */
    public List<Transaction> getTransazioniPremio() {
        return wallet.getTransazioni();
    }

    public RappresentazioneStato getRappresentazioneStato() {
        return this.state.getRappresentazioneStato();
    }

    @Override
    public String dettagliConto() {
        return this.datiHackathon.nome();
    }

    private Iscrizione requireIscrizione(Team team) {
        Iscrizione iscrizione = iscritti.get(team);
        if (iscrizione == null) {
            throw new NoSuchElementException("Il team non risulta iscritto all'hackathon");
        }
        return iscrizione;
    }
}
