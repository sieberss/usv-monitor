package de.sieberss.backend.model;

public record CredentialsDTO(
        String id,
        String user,
        String password,
        boolean global
) {
}
