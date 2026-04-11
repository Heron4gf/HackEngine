package it.unicam.ids2026.core.hackathon.status;

public enum RappresentazioneStato {

    ISCRIZIONE("In Iscrizione", "Si accettano iscrizioni da parte dei team"),
    IN_CORSO("In Corso", "L'Hackathon è in svolgimento, non è possibile iscriversi, è possibile inviare sottomissioni"),
    VALUTAZIONE("In Valutazione", "I giudici stanno valutando le sottomissioni"),
    CONCLUSO("Concluso", "I risultati sono disponibili");

    RappresentazioneStato(String name, String description) {

    }
}
