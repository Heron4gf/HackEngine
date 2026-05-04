package it.unicam.ids2026.core.hackathon.data;

import it.unicam.ids2026.core.transaction.MoneyAmount;
import lombok.NonNull;

public record DatiHackathon(
        @NonNull String nome,
        @NonNull String luogo,
        @NonNull MoneyAmount premioInDenaro,
        int dimensioneMaxTeam,
        @NonNull String regolamento
) {}