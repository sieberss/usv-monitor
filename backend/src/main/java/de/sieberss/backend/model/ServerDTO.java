package de.sieberss.backend.model;

public record ServerDTO(
        String id,
        String name,
        String address,
        CredentialsDTO credentials,
        String upsId,
        int shutdownTime) {
}
