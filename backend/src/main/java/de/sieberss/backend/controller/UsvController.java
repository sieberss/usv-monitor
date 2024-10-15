package de.sieberss.backend.controller;

import de.sieberss.backend.model.Usv;
import de.sieberss.backend.service.UsvService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.List;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/api/usv")
@RequiredArgsConstructor
public class UsvController {
    private final UsvService service;

    @GetMapping()
    public List<Usv> getUsvList() {
        return service.getUsvList();
    }

    @GetMapping("/{id}")
    public Usv getUsvById(@PathVariable String id) {
        return service.getUsvById(id);
    }

    @ExceptionHandler(NoSuchElementException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorMessage handleNoSuchElementException(NoSuchElementException e) {
        return new ErrorMessage("Not found", e.getMessage(), Instant.now());
    }

}
