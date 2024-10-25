package de.sieberss.backend.model;

import org.springframework.data.mongodb.core.mapping.DocumentReference;

public record Server(
        String id,
        String name,
        String address,
        Credentials credentials,
        @DocumentReference
        Ups ups,
        int shutdownTime) {
}
