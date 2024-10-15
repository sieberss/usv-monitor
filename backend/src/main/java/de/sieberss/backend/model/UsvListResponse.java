package de.sieberss.backend.model;

import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class UsvListResponse {
    private final boolean monitoring;
    private final List<Usv> uvs;
}
