package de.sieberss.backend.model;

import java.time.Instant;

public record Status(
        String upsOrServerId,
        PowerState state,
        Instant timestamp,
        long remaining
) {
}
