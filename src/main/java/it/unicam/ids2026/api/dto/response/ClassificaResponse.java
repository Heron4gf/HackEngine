package it.unicam.ids2026.api.dto.response;

import it.unicam.ids2026.core.roles.team.Team;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Response DTO per la classifica di un hackathon.
 */
public record ClassificaResponse(
        UUID hackathonId,
        String nomeHackathon,
        List<VoceClassifica> classifica
) {
    public record VoceClassifica(
            int posizione,
            String nomeTeam,
            int voto
    ) {}

    /**
     * Crea un ClassificaResponse da una lista di (Team, voto) entries.
     *
     * @param hackathonId ID dell'hackathon
     * @param nomeHackathon nome dell'hackathon
     * @param entries lista di coppie (Team, voto) già ordinata per voto decrescente
     * @return ClassificaResponse
     */
    public static ClassificaResponse from(UUID hackathonId, String nomeHackathon, List<Map.Entry<Team, Integer>> entries) {
        List<VoceClassifica> voci = entries.stream()
                .map(entry -> new VoceClassifica(
                        0, // posizione calcolata dopo
                        entry.getKey().getNome(),
                        entry.getValue()
                ))
                .toList();

        // Calcola le posizioni (gestisce i parimerito)
        List<VoceClassifica> vociConPosizione = new ArrayList<>();
        int posizioneCorrente = 1;
        int ultimoVoto = -1;
        int posizionePerVoto = 1;

        for (VoceClassifica voce : voci) {
            if (voce.voto() != ultimoVoto) {
                posizionePerVoto = posizioneCorrente;
                ultimoVoto = voce.voto();
            }
            vociConPosizione.add(new VoceClassifica(
                    posizionePerVoto,
                    voce.nomeTeam(),
                    voce.voto()
            ));
            posizioneCorrente++;
        }

        return new ClassificaResponse(hackathonId, nomeHackathon, vociConPosizione);
    }
}
