package de.sieberss.backend.model;

public record Server(
        String id,
        String name,
        String address,
        Credentials credentials
) {
}
