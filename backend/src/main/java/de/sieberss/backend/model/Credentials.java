package de.sieberss.backend.model;

import lombok.Builder;

@Builder
public record Credentials(
        String id,
        String user,
        String password,
        boolean global
) {
}
