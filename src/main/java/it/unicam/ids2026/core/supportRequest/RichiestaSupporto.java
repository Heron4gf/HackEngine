package it.unicam.ids2026.core.supportRequest;

import it.unicam.ids2026.core.roles.staff.Mentore;
import it.unicam.ids2026.core.supportRequest.response.RispostaRichiesta;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class RichiestaSupporto {
    private String titolo;
    private StatoRichiesta stato;
    private String descrizione;
    private Mentore incaricato;
    private RispostaRichiesta risposta;
}
