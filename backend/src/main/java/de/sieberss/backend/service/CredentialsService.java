package de.sieberss.backend.service;

import de.sieberss.backend.model.Credentials;
import de.sieberss.backend.model.CredentialsDTO;
import de.sieberss.backend.repo.CredentialsRepo;
import de.sieberss.backend.utils.IdService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.encrypt.Encryptors;
import org.springframework.security.crypto.encrypt.TextEncryptor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;


@Service
@RequiredArgsConstructor
public class CredentialsService {

    private final CredentialsRepo repo;
    private final IdService idService;

    @Value("${encryption.salt}")
    private String salt;

    private boolean missingData(CredentialsDTO unencrypted) {
        return unencrypted == null || unencrypted.user() == null || unencrypted.password() == null
                || unencrypted.user().isEmpty() || unencrypted.password().isEmpty();
    }

    CredentialsDTO decryptCredentials(Credentials credentials) {
        TextEncryptor encryptor = Encryptors.text(credentials.encryptionKey(), salt);
        String decryptedPassword = encryptor.decrypt(credentials.password());
        return new CredentialsDTO(credentials.id(), credentials.user(), decryptedPassword, credentials.global());
    }

    Credentials getEncryptedCredentials(CredentialsDTO credentials) {
        return repo.findById(credentials.id()).orElseThrow(NoSuchElementException::new);
    }

    Credentials createEncryptedCredentialsFromDTO(CredentialsDTO dto) {
        String id = idService.generateId();
        String encryptionKey = idService.generateId();
        TextEncryptor encryptor = Encryptors.text(encryptionKey, salt);
        String encryptedPassword = encryptor.encrypt(dto.password());
        return new Credentials(id, dto.user(), encryptedPassword, encryptionKey, dto.global());
    }

    Credentials updateEncryptedCredentialsFromDTO(Credentials dbObject, CredentialsDTO dto) {
        TextEncryptor encryptor = Encryptors.text(dbObject.encryptionKey(), salt);
        String encryptedPassword = encryptor.encrypt(dto.password());
        return new Credentials(dbObject.id(), dto.user(), encryptedPassword, dbObject.encryptionKey(), dto.global());
    }

    public List<CredentialsDTO> getCredentialsList() {
        return repo.findAll()
                .stream()
                .map(this::decryptCredentials)
                .toList();
    }

    public CredentialsDTO getCredentialsById(String id) {
        Credentials dbObject = repo.findById(id).orElseThrow(()->new NoSuchElementException(id));
        return decryptCredentials(dbObject);
    }

    public CredentialsDTO createCredentials(CredentialsDTO submitted) {
        if (missingData(submitted))
            throw new IllegalArgumentException("Missing username and/or password");
        Credentials dbObject = createEncryptedCredentialsFromDTO(submitted);
        repo.save(dbObject);
        return decryptCredentials(dbObject);
    }

    public CredentialsDTO updateCredentials(String id, CredentialsDTO submitted) {
        Credentials dbObject = repo.findById(id).orElseThrow(() -> new NoSuchElementException(id));
        if (missingData(submitted))
            throw new IllegalArgumentException("Missing username and/or password");
        dbObject = updateEncryptedCredentialsFromDTO(dbObject, submitted);
        repo.save(dbObject);
        return decryptCredentials(dbObject);
    }

    public void deleteCredentials(String id) {
        if (!repo.existsById(id))
            throw new NoSuchElementException(id);
        repo.deleteById(id);
    }

}
