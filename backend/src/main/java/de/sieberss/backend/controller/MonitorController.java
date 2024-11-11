package de.sieberss.backend.controller;

import de.sieberss.backend.exception.ErrorMessage;
import de.sieberss.backend.exception.MonitoringStartFailedException;
import de.sieberss.backend.exception.MonitoringStopFailedException;
import de.sieberss.backend.model.StatusResponse;
import de.sieberss.backend.service.MonitorService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;

@RestController
@RequestMapping("/api/monitor")
@RequiredArgsConstructor
public class MonitorController {

    private final MonitorService monitorService;

    @GetMapping
    public StatusResponse getAllStatuses(){
        return monitorService.getAllStatuses();
    }

    @PostMapping
    public boolean changeMode(@RequestParam boolean monitoring){
        return monitorService.changeMode(monitoring);
    }

    @ExceptionHandler(MonitoringStartFailedException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorMessage monitoringStartFailed(MonitoringStartFailedException e) {
        return new ErrorMessage("Could not start monitoring", e.getMessage(), Instant.now());
    }

    @ExceptionHandler(MonitoringStopFailedException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorMessage monitoringStopFailed(MonitoringStopFailedException e) {
        return new ErrorMessage("Could not stop monitoring", e.getMessage(), Instant.now());
    }
}
