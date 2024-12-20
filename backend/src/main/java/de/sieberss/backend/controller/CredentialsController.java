package de.sieberss.backend.controller;

import de.sieberss.backend.exception.ErrorMessage;
import de.sieberss.backend.model.CredentialsWithoutEncryption;
import de.sieberss.backend.service.CredentialsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.List;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/api/credentials")
@RequiredArgsConstructor
public class CredentialsController {
    private final CredentialsService service;

    @GetMapping()
    public List<CredentialsWithoutEncryption> getCredentialsList() {
        return service.getCredentialsList();
    }

    @GetMapping("/{id}")
    public CredentialsWithoutEncryption getCredentialsById(@PathVariable String id) {
        return service.getCredentialsById(id);
    }

    @PostMapping()
    public CredentialsWithoutEncryption createCredentials(@RequestBody CredentialsWithoutEncryption credentials) {
        return service.createCredentials(credentials);
    }

    @PutMapping("/{id}")
    public CredentialsWithoutEncryption updateCredentials(@PathVariable String id, @RequestBody CredentialsWithoutEncryption credentials) {
        return service.updateCredentials(id, credentials);
    }
    @DeleteMapping("/{id}")
    public void deleteCredentials(@PathVariable String id) {
        service.deleteCredentials(id);
    }

    @ExceptionHandler(NoSuchElementException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorMessage handleNoSuchElementException(NoSuchElementException e) {
        return new ErrorMessage("Credentials not found", e.getMessage(), Instant.now());
    }

}
