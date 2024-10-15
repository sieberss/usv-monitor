package de.sieberss.backend.model;

import java.time.Instant;

public record Event(
        String id,
        PowerState eventType,
        Instant time,
        String occuredAtId
) {
}
