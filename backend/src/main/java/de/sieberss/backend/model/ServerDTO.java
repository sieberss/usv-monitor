package de.sieberss.backend.model;

public record ServerDTO(
        String id,
        String name,
        String address,
        CredentialsWithoutEncryption credentials,
        String upsId
) {
}
