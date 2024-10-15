package de.sieberss.backend.controller;

import de.sieberss.backend.model.UsvListResponse;
import de.sieberss.backend.service.UsvService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/usv")
@RequiredArgsConstructor
public class UsvController {
    private final UsvService service;

    @GetMapping
    public UsvListResponse getUsvList() {
        return service.getAllUsvs();
    }
}
