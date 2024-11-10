package de.sieberss.backend.service;

import de.sieberss.backend.exception.MonitoringStartFailedException;
import de.sieberss.backend.exception.MonitoringStopFailedException;
import de.sieberss.backend.model.StatusResponse;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

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
        else return new StatusResponse(false, new ArrayList<>());
    }

    public boolean changeMode(boolean monitoring) {
        /* nothing to do when mode unchanged */
        if (monitoring == statusService.isMonitoring()) {
            return monitoring;
        }
        if (monitoring) {
            try {
                statusService.startMonitoring();
                return true;
            }
            catch (MonitoringStartFailedException e) {
                // now exception yet in simulation
            }
        }
        else {
            try {
                statusService.stopMonitoring();
                return false;
            }
            catch (MonitoringStopFailedException e){
                // now exception yet in simulation
            }
        }
        return monitoring;
    }

}

