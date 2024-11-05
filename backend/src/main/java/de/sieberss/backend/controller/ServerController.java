package de.sieberss.backend.controller;

import de.sieberss.backend.exception.ErrorMessage;
import de.sieberss.backend.model.ServerDTO;
import de.sieberss.backend.model.ServerDTOWithoutCredentialsId;
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
    public List<ServerDTO> getServerDTOList() {
        return service.getServerDTOList();
    }

    @GetMapping("/{id}")
    public ServerDTO getServerDTOById(@PathVariable String id) {
        return service.getServerDTOById(id);
    }

    @PostMapping()
    public ServerDTO createServer(@RequestBody ServerDTO serverDTO) {
        return service.createServer(serverDTO);
    }

    @PutMapping("/{id}")
    public ServerDTO updateServer(@PathVariable String id, @RequestBody ServerDTO serverDTO) {
        return service.updateServer(id, serverDTO);
    }
    @DeleteMapping("/{id}")
    public void deleteServer(@PathVariable String id) {
        service.deleteServer(id);
    }

    @PostMapping("/localcredentials")
    public ServerDTO createServerWithNewLocalCredentials(@RequestBody ServerDTOWithoutCredentialsId dto) {
        return service.createServerWithNewLocalCredentials(dto);
    }

    @PutMapping("/localcredentials/{id}")
    public ServerDTO updateServerWithNewLocalCredentials(@PathVariable String id, @RequestBody ServerDTOWithoutCredentialsId dto) {
        return service.updateServerWithNewLocalCredentials(id, dto);
    }

    @ExceptionHandler(NoSuchElementException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorMessage handleNoSuchElementException(NoSuchElementException e) {
        return new ErrorMessage("Server not found", e.getMessage(), Instant.now());
    }

}
