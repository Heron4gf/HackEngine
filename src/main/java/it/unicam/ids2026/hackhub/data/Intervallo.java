package it.unicam.ids2026.hackhub.data;

import java.util.Date;

public class Intervallo {
    private Date dataInizio;
    private Date dataFine;

    public boolean dataCompresa(Date data) {
        if (data == null || dataInizio == null || dataFine == null) {
            return false;
        }
        return !data.before(dataInizio) && !data.after(dataFine);
    }

    public boolean isValidInterval() {
        if (dataInizio == null || dataFine == null) {
            return false;
        }
        return !dataFine.before(dataInizio);
    }
}