package de.sieberss.backend.controller;

import de.sieberss.backend.model.Server;
import de.sieberss.backend.service.ServerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.List;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/api/server")
@RequiredArgsConstructor
public class ServerController {
    private final ServerService service;

    @GetMapping()
    public List<Server> getServerList() {
        return service.getServerList();
    }

    @GetMapping("/{id}")
    public Server getServerById(@PathVariable String id) {
        return service.getServerById(id);
    }

    @PostMapping()
    public Server createServer(@RequestBody Server server) {
        return service.createServer(server);
    }

    @PutMapping("/{id}")
    public Server updateServer(@PathVariable String id, @RequestBody Server server) {
        return service.updateServer(id, server);
    }
    @DeleteMapping("/{id}")
    public void deleteServer(@PathVariable String id) {
        service.deleteServer(id);
    }

    @ExceptionHandler(NoSuchElementException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorMessage handleNoSuchElementException(NoSuchElementException e) {
        return new ErrorMessage("Server not found", e.getMessage(), Instant.now());
    }

}
