package de.sieberss.backend.service;

import de.sieberss.backend.model.Credentials;
import de.sieberss.backend.model.CredentialsDTO;
import de.sieberss.backend.repo.CredentialsRepo;
import de.sieberss.backend.utils.EncryptionService;
import de.sieberss.backend.utils.IdService;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;


@Service
@RequiredArgsConstructor
public class CredentialsService {

    private final CredentialsRepo repo;
    private final IdService idService;

    @Value("${encryption.key}")
    private String encryptionKey;

    @PostConstruct
    public void init() {
        EncryptionService.setApplicationKey(encryptionKey);
    }
    private boolean missingData(CredentialsDTO unencrypted) {
        return unencrypted == null || unencrypted.user() == null || unencrypted.password() == null
                || unencrypted.user().isEmpty() || unencrypted.password().isEmpty();
    }

    private CredentialsDTO decryptAllExceptAppUsers(Credentials credentials) {
        return credentials.user().startsWith("APP/")
                ? new CredentialsDTO(credentials.id(), credentials.user(), credentials.password(), credentials.global())
                : EncryptionService.decryptCredentials(credentials);
    }

    public List<CredentialsDTO> getCredentialsList() {
        return repo.findAll()
                .stream()
                .map(this::decryptAllExceptAppUsers)
                .toList();
    }

    public CredentialsDTO getCredentialsById(String id) {
        Credentials dbObject = repo.findById(id).orElseThrow(()->new NoSuchElementException(id));
        return EncryptionService.decryptCredentials(dbObject);
    }

    public CredentialsDTO createCredentials(CredentialsDTO submitted) {
        if (missingData(submitted))
            throw new IllegalArgumentException("Missing username and/or password");
        CredentialsDTO completed =
                new CredentialsDTO(idService.generateId(), submitted.user(), submitted.password(), submitted.global());
        Credentials dbObject = EncryptionService.encryptCredentials(completed);
        repo.save(dbObject);
        return completed;
    }

    public CredentialsDTO updateCredentials(String id, CredentialsDTO submitted) {
        if (!repo.existsById(id))
            throw new NoSuchElementException(id);
        if (missingData(submitted))
            throw new IllegalArgumentException("Missing username and/or password");
        CredentialsDTO completed = new CredentialsDTO(id, submitted.user(), submitted.password(), submitted.global());
        Credentials dbObject = EncryptionService.encryptCredentials(completed);
        repo.save(dbObject);
        return completed;
    }

    public void deleteCredentials(String id) {
        if (!repo.existsById(id))
            throw new NoSuchElementException(id);
        repo.deleteById(id);
    }

}
