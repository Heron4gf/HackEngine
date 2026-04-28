package it.unicam.ids2026.api.dto.response;

import it.unicam.ids2026.core.hackathon.Hackathon;
import it.unicam.ids2026.core.hackathon.status.RappresentazioneStato;
import it.unicam.ids2026.core.transaction.MoneyAmount;

import java.util.UUID;

public record HackathonResponse(
        UUID id,
        String nome,
        String luogo,
        MoneyAmount premioInDenaro,
        int dimensioneMaxTeam,
        String regolamento,
        RappresentazioneStato stato,
        UUID organizzatoreId,
        UUID giudiceId
) {
    public static HackathonResponse from(Hackathon hackathon) {
        return new HackathonResponse(
                hackathon.getId(),
                hackathon.getDatiHackathon().nome(),
                hackathon.getDatiHackathon().luogo(),
                hackathon.getDatiHackathon().premioInDenaro(),
                hackathon.getDatiHackathon().dimensioneMaxTeam(),
                hackathon.getDatiHackathon().regolamento(),
                hackathon.getRappresentazioneStato(),
                hackathon.getOrganizzatore().getId(),
                hackathon.getGiudice().getId()
        );
    }
}
