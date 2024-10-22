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

    private String encrypt(String value) {
        try {
            return encryptionService.encrypt(value, key);
        } catch (Exception e) {
            throw new EncryptionException(e.getMessage());
        }
    }

    public static void main(String[] args) throws Exception {
        System.out.println(new EncryptionService().generateKey());
    }
    private String decrypt(String value) {
        try {
            return encryptionService.decrypt(value, key);
        }
        catch (Exception e) {
            throw new EncryptionException(e.getMessage());
        }
    }


    public List<CredentialsWithoutEncryption> getCredentialsList() {
        return repo.findAll()
                .stream()
                .map(c -> {
                    try {
                        return new CredentialsWithoutEncryption(c.id(), c.user(), decrypt(c.password()));
                    } catch (Exception e) {
                        throw new EncryptionException(e.getMessage());
                    }
                })
                .toList();
    }

    public CredentialsWithoutEncryption getCredentialsById(String id) {
        Credentials dbObject = repo.findById(id).orElseThrow(()->new NoSuchElementException(id));
        return new CredentialsWithoutEncryption(dbObject.id(), dbObject.user(), decrypt(dbObject.password()));
    }

    public CredentialsWithoutEncryption createCredentials(CredentialsWithoutEncryption submitted) {
        Credentials dbObject= repo.save(new Credentials(
                idService.generateId(), submitted.user(), encrypt(submitted.password())));
        return new CredentialsWithoutEncryption(dbObject.id(), submitted.user(), submitted.password());
    }

    public CredentialsWithoutEncryption updateCredentials(String id, CredentialsWithoutEncryption submitted) {
        if (!repo.existsById(id))
            throw new NoSuchElementException(id);
        repo.save(new Credentials(id, submitted.user(), encrypt(submitted.password())));
        return new CredentialsWithoutEncryption(id, submitted.user(), submitted.password());
    }

    public void deleteCredentials(String id) {
        if (!repo.existsById(id))
            throw new NoSuchElementException(id);
        repo.deleteById(id);
    }

}
