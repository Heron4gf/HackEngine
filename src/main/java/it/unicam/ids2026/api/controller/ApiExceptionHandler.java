package it.unicam.ids2026.api.controller;

import it.unicam.ids2026.api.dto.response.MessageResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.NoSuchElementException;

/**
 * Gestisce le eccezioni a livello API, convertendole in risposte HTTP appropriate.
 */
@RestControllerAdvice
public class ApiExceptionHandler {

    /**
     * Gestisce le eccezioni di entità non trovata.
     *
     * @param exception eccezione lanciata
     * @return risposta 404 con messaggio di errore
     */
    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<MessageResponse> handleNotFound(NoSuchElementException exception) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new MessageResponse(exception.getMessage()));
    }

    /**
     * Gestisce le eccezioni di parametri non validi e cast errati.
     *
     * @param exception eccezione lanciata
     * @return risposta 400 con messaggio di errore
     */
    @ExceptionHandler({IllegalArgumentException.class, ClassCastException.class})
    public ResponseEntity<MessageResponse> handleBadRequest(RuntimeException exception) {
        return ResponseEntity.badRequest()
                .body(new MessageResponse(exception.getMessage()));
    }

    /**
     * Gestisce le eccezioni di stato non valido.
     *
     * @param exception eccezione lanciata
     * @return risposta 409 con messaggio di errore
     */
    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<MessageResponse> handleConflict(IllegalStateException exception) {
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(new MessageResponse(exception.getMessage()));
    }

    /**
     * Gestisce le eccezioni di validazione dei parametri di richiesta.
     *
     * @param exception eccezione di validazione
     * @return risposta 400 con messaggio di errore dettagliato
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<MessageResponse> handleValidation(MethodArgumentNotValidException exception) {
        String message = exception.getBindingResult().getFieldErrors().stream()
                .findFirst()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .orElse("Richiesta non valida");
        return ResponseEntity.badRequest().body(new MessageResponse(message));
    }
}
