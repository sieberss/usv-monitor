package de.sieberss.backend.exception;

import com.mongodb.MongoException;
import de.sieberss.backend.controller.ErrorMessage;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Instant;

@RestControllerAdvice
@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
public class GlobalExceptionHandler {

    @ExceptionHandler(MongoException.class)
    public ErrorMessage handleMongoException(MongoException e) {
        return new ErrorMessage("Database error", e.getMessage(), Instant.now());
    }

    @ExceptionHandler(EncryptionException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorMessage handleEncryptionException(EncryptionException e) {
        return new ErrorMessage("Encryption failed", e.getMessage(), Instant.now());
    }
}