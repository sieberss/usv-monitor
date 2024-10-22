package de.sieberss.backend.service;

import de.sieberss.backend.exception.EncryptionException;
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
    private final String key = System.getenv("ENCRYPT_KEY");

    private String encryptPassword(String value) {
        try {
            return encryptionService.encrypt(value, key);
        } catch (Exception e) {
            throw new EncryptionException(e.getMessage());
        }
    }

    private Credentials encryptCredentials(CredentialsWithoutEncryption unencrypted) {
        return new Credentials(unencrypted.id(), unencrypted.user(), encryptPassword(unencrypted.password()));
    }

     private String decryptPassword(String value) {
        try {
            return encryptionService.decrypt(value, key);
        }
        catch (Exception e) {
            throw new EncryptionException(e.getMessage());
        }
    }

    private CredentialsWithoutEncryption decryptCredentials(Credentials encrypted) {
        return new CredentialsWithoutEncryption(encrypted.id(), encrypted.user(), decryptPassword(encrypted.password()));
    }


    public List<CredentialsWithoutEncryption> getCredentialsList() {
        return repo.findAll()
                .stream()
                .map(this::decryptCredentials)
                .toList();
    }

    public CredentialsWithoutEncryption getCredentialsById(String id) {
        Credentials dbObject = repo.findById(id).orElseThrow(()->new NoSuchElementException(id));
        return decryptCredentials(dbObject);
    }

    public CredentialsWithoutEncryption createCredentials(CredentialsWithoutEncryption submitted) {
        // add id to submitted data
        CredentialsWithoutEncryption unencrypted =
                new CredentialsWithoutEncryption(idService.generateId(), submitted.user(), submitted.password());
        // store encrypted version in database, return unencrypted version
        repo.save(encryptCredentials(unencrypted));
        return unencrypted;
    }

    public CredentialsWithoutEncryption updateCredentials(String id, CredentialsWithoutEncryption submitted) {
        if (!repo.existsById(id))
            throw new NoSuchElementException(id);
        // store encrypted version in database, return unencrypted version
        repo.save(encryptCredentials(submitted));
        return submitted;
    }

    public void deleteCredentials(String id) {
        if (!repo.existsById(id))
            throw new NoSuchElementException(id);
        repo.deleteById(id);
    }

}
