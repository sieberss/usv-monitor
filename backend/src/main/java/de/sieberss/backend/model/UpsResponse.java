package de.sieberss.backend.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpsResponse {
    private Ups ups;
    private Event latestEvent;
}
