package de.sieberss.backend.model;

import lombok.With;

import java.time.Instant;
@With
public record Status(
        String upsOrServerId,
        PowerState state,
        Instant timestamp,
        long remaining
) {
}
