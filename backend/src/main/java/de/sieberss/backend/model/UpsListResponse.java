package de.sieberss.backend.model;

import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class UpsListResponse {
    private final boolean monitoring;
    private final List<Ups> uvs;
}
