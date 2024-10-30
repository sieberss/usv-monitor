package de.sieberss.backend.model;

public record ServerDTOWithoutCredentialsId(
        String id,
        String name,
        String address,
        String user,
        String password,
        String upsId,
        int shutdownTime) {
}
