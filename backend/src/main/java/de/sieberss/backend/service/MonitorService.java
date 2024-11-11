package de.sieberss.backend.service;

import de.sieberss.backend.model.StatusResponse;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Service;

import java.util.HashMap;

@Service
@RequiredArgsConstructor
@Getter
@Setter
public class MonitorService {

    private final StatusService statusService;

    public StatusResponse getAllStatuses() {
        if (statusService.isMonitoring()) {
            return new StatusResponse(true, statusService.getAllStatuses());
        }
        /* empty list when not monitoring */
        else return new StatusResponse(false, new HashMap<>());
    }

    public boolean changeMode(boolean monitoring) {
        /* nothing to do when mode unchanged */
        if (monitoring == statusService.isMonitoring()) {
            return monitoring;
        }
        return statusService.toggleMonitoring();
    }

}

