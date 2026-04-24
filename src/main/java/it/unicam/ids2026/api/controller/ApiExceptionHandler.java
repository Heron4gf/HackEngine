package it.unicam.ids2026.api.controller;

import it.unicam.ids2026.api.dto.response.MessageResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.NoSuchElementException;

@RestControllerAdvice
public class ApiExceptionHandler {

    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<MessageResponse> handleNotFound(NoSuchElementException exception) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new MessageResponse(exception.getMessage()));
    }

    @ExceptionHandler({IllegalArgumentException.class, ClassCastException.class})
    public ResponseEntity<MessageResponse> handleBadRequest(RuntimeException exception) {
        return ResponseEntity.badRequest()
                .body(new MessageResponse(exception.getMessage()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<MessageResponse> handleValidation(MethodArgumentNotValidException exception) {
        String message = exception.getBindingResult().getFieldErrors().stream()
                .findFirst()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .orElse("Richiesta non valida");
        return ResponseEntity.badRequest().body(new MessageResponse(message));
    }
}
