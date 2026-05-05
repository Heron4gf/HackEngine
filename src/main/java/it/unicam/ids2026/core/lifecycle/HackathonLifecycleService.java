package it.unicam.ids2026.core.lifecycle;

import it.unicam.ids2026.core.hackathon.status.RappresentazioneStato;
import it.unicam.ids2026.core.managers.HackathonManager;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

/**
 * Servizio responsabile della gestione automatica del ciclo di vita degli hackathon.
 *
 * <p>Verifica periodicamente gli hackathon in stato {@link RappresentazioneStato#IN_CORSO}
 * e, qualora la loro durata sia scaduta (ovvero {@code durataHackathon.dataFine()} precede
 * la data odierna), ne avanza automaticamente lo stato a {@link RappresentazioneStato#VALUTAZIONE}
 * tramite {@link HackathonManager#avanzaStato}.</p>
 *
 * <p>Il controllo viene eseguito ogni ora. Poiché {@code Intervallo} utilizza
 * {@code LocalDate}, la granularità oraria è più che sufficiente.</p>
 */
@Service
@RequiredArgsConstructor
public class HackathonLifecycleService {

    private final HackathonManager hackathonManager;

    /**
     * Controlla ogni ora gli hackathon in corso e avanza automaticamente
     * quelli la cui durata è terminata allo stato di valutazione.
     *
     * <p>Il metodo filtra solo gli hackathon nello stato {@code IN_CORSO} e
     * confronta la data di fine della durata con la data corrente. Se la durata
     * è scaduta, delega l'avanzamento di stato a {@link HackathonManager#avanzaStato}.</p>
     */
    @Scheduled(cron = "0 0 * * * *")
    public void sincronizzaStatiHackathon() {
        LocalDate oggi = LocalDate.now();

        // Filtra solo gli hackathon in corso la cui durata è già terminata
        hackathonManager.getHackathons().stream()
                .filter(h -> h.getRappresentazioneStato() == RappresentazioneStato.IN_CORSO)
                .filter(h -> oggi.isAfter(h.getDurataHackathon().dataFine().toLocalDate()))
                .forEach(hackathonManager::avanzaStato);
    }
}
