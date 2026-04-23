package it.unicam.ids2026.core.roles.team;

import it.unicam.ids2026.core.hackathon.data.Sottomissione;
import it.unicam.ids2026.core.supportRequest.RichiestaSupporto;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Iscrizione {
    private Sottomissione sottomissione = null;
    private RichiestaSupporto richiestaSupporto = null;
}
