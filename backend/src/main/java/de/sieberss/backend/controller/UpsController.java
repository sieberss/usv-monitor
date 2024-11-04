package de.sieberss.backend.controller;

import de.sieberss.backend.exception.ErrorMessage;
import de.sieberss.backend.model.Ups;
import de.sieberss.backend.service.UpsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.List;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/api/ups")
@RequiredArgsConstructor
public class UpsController {
    private final UpsService service;

    @GetMapping()
    public List<Ups> getUpsList() {
        return service.getUpsList();
    }

    @GetMapping("/{id}")
    public Ups getUpsById(@PathVariable String id) {
        return service.getUpsById(id);
    }

    @PostMapping()
    public Ups createUps(@RequestBody Ups ups) {
        return service.createUps(ups);
    }

    @PutMapping("/{id}")
    public Ups updateUps(@PathVariable String id, @RequestBody Ups ups) {
        return service.updateUps(id, ups);
    }
    @DeleteMapping("/{id}")
    public void deleteUps(@PathVariable String id) {
        service.deleteUps(id);
    }

    @ExceptionHandler(NoSuchElementException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorMessage handleNoSuchElementException(NoSuchElementException e) {
        return new ErrorMessage("UPS not found", e.getMessage(), Instant.now());
    }

}
