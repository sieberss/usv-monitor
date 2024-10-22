package de.sieberss.backend.model;

public record CredentialsWithoutEncryption(
        String id,
        String user,
        String password
) {
}
