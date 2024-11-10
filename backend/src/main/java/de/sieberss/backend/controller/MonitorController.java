package de.sieberss.backend.controller;

import de.sieberss.backend.model.StatusResponse;
import de.sieberss.backend.service.MonitorService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
    public boolean changeMode(boolean monitoring){
        return monitorService.changeMode(monitoring);
    }
}
