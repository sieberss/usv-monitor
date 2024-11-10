package de.sieberss.backend.controller;

import de.sieberss.backend.model.StatusResponse;
import de.sieberss.backend.service.MonitorService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

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
    public boolean changeMode(@RequestBody boolean monitoring){
        return monitorService.changeMode(monitoring);
    }
}
