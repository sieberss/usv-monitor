package de.sieberss.backend.exception;

import java.time.Instant;

public record ErrorMessage(
        String message,
        String id,
        Instant timestamp) {
}
