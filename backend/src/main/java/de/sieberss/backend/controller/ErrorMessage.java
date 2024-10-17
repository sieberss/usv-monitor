package de.sieberss.backend.controller;

import java.time.Instant;

public record ErrorMessage(
        String message,
        String id,
        Instant timestamp) {
}
