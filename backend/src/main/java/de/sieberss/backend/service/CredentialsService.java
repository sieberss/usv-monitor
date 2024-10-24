package de.sieberss.backend.service;

import de.sieberss.backend.model.Credentials;
import de.sieberss.backend.model.CredentialsWithoutEncryption;
import de.sieberss.backend.repo.CredentialsRepo;
import de.sieberss.backend.utils.EncryptionService;
import de.sieberss.backend.utils.IdService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.NoSuchElementException;


@Service
@RequiredArgsConstructor
public class CredentialsService {

    private final CredentialsRepo repo;
    private final IdService idService;
    private final EncryptionService encryptionService;

    public List<CredentialsWithoutEncryption> getCredentialsList() {
        return repo.findAll()
                .stream()
                .map(encryptionService::decryptCredentials)
                .toList();
    }

    public CredentialsWithoutEncryption getCredentialsById(String id) {
        Credentials dbObject = repo.findById(id).orElseThrow(()->new NoSuchElementException(id));
        return encryptionService.decryptCredentials(dbObject);
    }

    public CredentialsWithoutEncryption createCredentials(CredentialsWithoutEncryption submitted) {
        CredentialsWithoutEncryption completed =
                new CredentialsWithoutEncryption(idService.generateId(), submitted.user(), submitted.password(), submitted.localOnly());
        Credentials dbObject = encryptionService.encryptCredentials(completed);
        repo.save(dbObject);
        return completed;
    }

    public CredentialsWithoutEncryption updateCredentials(String id, CredentialsWithoutEncryption submitted) {
        if (!repo.existsById(id))
            throw new NoSuchElementException(id);
        CredentialsWithoutEncryption completed = new CredentialsWithoutEncryption(id, submitted.user(), submitted.password(), submitted.localOnly());
        Credentials dbObject = encryptionService.encryptCredentials(completed);
        repo.save(dbObject);
        return completed;
    }

    public void deleteCredentials(String id) {
        if (!repo.existsById(id))
            throw new NoSuchElementException(id);
        repo.deleteById(id);
    }

}
