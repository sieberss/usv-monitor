package de.sieberss.backend.controller;

import de.sieberss.backend.model.Usv;
import de.sieberss.backend.model.UsvListResponse;
import de.sieberss.backend.service.UsvService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/usv")
@RequiredArgsConstructor
public class UsvController {
    private final UsvService service;

    @GetMapping()
    public List<Usv> getUsvList() {
        return service.getUsvList();
    }
}
