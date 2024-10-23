package de.sieberss.backend.model;

import org.springframework.data.mongodb.core.mapping.DocumentReference;

public record ServerDTO(
        String id,
        String name,
        String address,
        CredentialsWithoutEncryption credentials,
        @DocumentReference
        Ups ups
) {
}
