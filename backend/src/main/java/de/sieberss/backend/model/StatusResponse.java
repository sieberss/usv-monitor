package de.sieberss.backend.model;

import java.util.List;

public record StatusResponse(
        boolean monitoring,
        List<Status> statuses
) {
}
