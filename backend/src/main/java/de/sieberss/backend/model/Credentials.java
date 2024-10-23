package de.sieberss.backend.model;

public record Credentials(
        String id,
        String user,
        String password,
        boolean localOnly
) {
}
