package it.unicam.ids2026.core.hackathon.data;

import java.math.BigDecimal;
import java.util.Currency;

public record DatiHackathon(
        String nome,
        String luogo,
        BigDecimal premioInDenaro,
        Currency currency,
        int dimensioneMaxTeam,
        String regolamento
) {}