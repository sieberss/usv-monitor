package de.sieberss.backend.model;

import java.util.Map;

public record StatusResponse(
        boolean monitoring,
        Map<String, Status> statusMap
) {
}
