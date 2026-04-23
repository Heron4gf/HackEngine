package it.unicam.ids2026.core.supportRequest;

import it.unicam.ids2026.core.roles.staff.Mentore;
import it.unicam.ids2026.core.supportRequest.response.RispostaRichiesta;
import lombok.*;

@Getter
@Setter
@RequiredArgsConstructor

public class RichiestaSupporto {
    @NonNull
    private String titolo;
    private StatoRichiesta stato = StatoRichiesta.IN_ATTESA;
    @NonNull
    private String descrizione;
    private Mentore incaricato;
    private RispostaRichiesta risposta;
}
