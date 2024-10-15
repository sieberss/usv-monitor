package de.sieberss.backend.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UsvResponse {
    private Usv usv;
    private Event latestEvent;
}
