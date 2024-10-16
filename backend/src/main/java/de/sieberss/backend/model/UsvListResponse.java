package de.sieberss.backend.model;

import java.util.List;

public record UsvListResponse(boolean monitoring, List<UsvResponseObject> usvs) {}

