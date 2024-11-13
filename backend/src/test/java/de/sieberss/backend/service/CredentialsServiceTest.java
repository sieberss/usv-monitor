package de.sieberss.backend.service;

import de.sieberss.backend.model.Credentials;
import de.sieberss.backend.model.CredentialsDTO;
import de.sieberss.backend.repo.CredentialsRepo;
import de.sieberss.backend.utils.IdService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.encrypt.Encryptors;
import org.springframework.security.crypto.encrypt.TextEncryptor;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@SpringBootTest
class CredentialsServiceTest {

    private final CredentialsRepo repo = mock(CredentialsRepo.class);
    private final IdService idService = mock(IdService.class);
    private final CredentialsService service = new CredentialsService(repo, idService);
    private final String salt = "1234567890123456";

    @BeforeEach
    void setUp() {
        service.setTestSalt(salt);
    }

    @Test
    void getCredentialsList_shouldReturnEmptyList_whenDatabaseIsEmpty() {
        when(repo.findAll()).thenReturn(List.of());
        assertEquals(List.of(), service.getCredentialsList());
        verify(repo).findAll();
    }

    @Test
    void getCredentialsList_shouldReturnContent_whenRepoIsFilled() {
        String encryptionKey = "encryptionKey";
        TextEncryptor encryptor = Encryptors.text(encryptionKey, salt);
        String password = "secret";
        String encryptedPassword = encryptor.encrypt(password);
        CredentialsDTO unencrypted = new CredentialsDTO("1", "user", password, true);
        Credentials encrypted = new Credentials("1", "user", encryptedPassword, encryptionKey, true);
        when(repo.findAll()).thenReturn(List.of(encrypted));
        // execute tested method
        assertEquals(List.of(unencrypted), service.getCredentialsList());
        verify(repo).findAll();
    }

    @Test
    void getCredentialsById_shouldReturnObject_whenIdExists() {
        String encryptionKey = "encryptionKey";
        TextEncryptor encryptor = Encryptors.text(encryptionKey, salt);
        String password = "secret";
        String encryptedPassword = encryptor.encrypt(password);
        CredentialsDTO unencrypted = new CredentialsDTO("1", "user", password, true);
        Credentials encrypted = new Credentials("1", "user", encryptedPassword,encryptionKey, true);
        when(repo.findById("1")).thenReturn(Optional.of(encrypted));
        // execute tested method
        CredentialsDTO actual = service.getCredentialsById("1");
        assertEquals(unencrypted, actual);
        verify(repo).findById("1");
    }


    @Test
    void getCredentialsById_shouldThrowNoSuchElementException_whenIdDoesNotExist() {
        when(repo.findById("1")).thenReturn(Optional.empty());
        assertThrows(NoSuchElementException.class, () -> service.getCredentialsById("1"));
        verify(repo).findById("1");
    }

    @Test
    void createCredentials_shouldThrowIllegalArgumentException_whenUsernameEmpty() {
        when(idService.generateId()).thenReturn("1");
        CredentialsDTO submitted = new CredentialsDTO("", "", "pass", true);
        // execute tested method
        assertThrows(IllegalArgumentException.class, () -> service.createCredentials(submitted));
    }

    @Test
    void createCredentials_shouldThrowIllegalArgumentException_whenUsernameNull() {
        when(idService.generateId()).thenReturn("1");
        CredentialsDTO submitted = new CredentialsDTO("", null, "pass", true);
        // execute tested method
        assertThrows(IllegalArgumentException.class, () -> service.createCredentials(submitted));
    }

    @Test
    void createCredentials_shouldThrowIllegalArgumentException_whenPasswordEmpty() {
        when(idService.generateId()).thenReturn("1");
        CredentialsDTO submitted = new CredentialsDTO("", "user", "", true);
        // execute tested method
        assertThrows(IllegalArgumentException.class, () -> service.createCredentials(submitted));
    }

    @Test
    void createCredentials_shouldThrowIllegalArgumentException_whenPasswordNull() {
        when(idService.generateId()).thenReturn("1");
        CredentialsDTO submitted = new CredentialsDTO("", "user", null, true);
        // execute tested method
        assertThrows(IllegalArgumentException.class, () -> service.createCredentials(submitted));
    }

    @Test
    void createCredentials_shouldReturn_ObjectWithNewIdAndSubmittedData_whenUserAndPasswordProvided() {
        String salt = "1111111111111111";
        String encryptionKey = "encryptionKey";
        TextEncryptor encryptor = Encryptors.text(encryptionKey, salt);
        String password = "secret";
        String encryptedPassword = encryptor.encrypt(password);
        when(idService.generateId()).thenReturn("1", encryptionKey);
        CredentialsDTO submitted = new CredentialsDTO("", "user", password, true);
        CredentialsDTO expected = new CredentialsDTO("1", "user", password, true);
        Credentials encrypted = service.createEncryptedCredentialsFromDTO(submitted);
        when(repo.save(encrypted)).thenReturn(encrypted);
        // execute tested method
        CredentialsDTO actual = service.createCredentials(submitted);
        assertEquals(expected, actual);
        verify(repo).save(encrypted);
        verify(idService).generateId();
    }

    @Test
    void updateCredentials_shouldReturnUpdatedObject_whenIdExists() {
        String encryptionKey = "encryptionKey";
        TextEncryptor encryptor = Encryptors.text(encryptionKey, salt);
        String password = "secret";
        String encryptedPassword = encryptor.encrypt(password);
        CredentialsDTO submitted = new CredentialsDTO("null", "user", password, true);
        CredentialsDTO expected = new CredentialsDTO("1", "user", password, true);
        Credentials encrypted = new Credentials("1", "user", encryptedPassword, encryptionKey, true);
        Credentials oldValue = new Credentials("1", "userx", "JKJJKK", encryptionKey, true);
        when(repo.findById("1")).thenReturn(Optional.of(oldValue));
        when(repo.save(encrypted)).thenReturn(encrypted);
        // execute tested method
        CredentialsDTO actual = service.updateCredentials("1", submitted);
        assertEquals(expected, actual);
        verify(repo).findById("1");
        verify(repo).save(encrypted);
    }

    @Test
    void updateCredentials_shouldThrowNoSuchElementException_whenIdDoesNotExist() {
        CredentialsDTO submitted = new CredentialsDTO("null", "user", "pass", true);
        assertThrows(NoSuchElementException.class, () -> service.updateCredentials("1", submitted));
        verify(repo).existsById("1");
    }

    @Test
    void deleteCredentials_shouldDeleteObject_whenIdExists() {
        when(repo.existsById("1")).thenReturn(true);
        service.deleteCredentials("1");
        verify(repo).existsById("1");
        verify(repo).deleteById("1");
    }

    @Test
    void deleteCredentials_shouldThrowNoSuchElementException_whenIdDoesNotExist() {
        assertThrows(NoSuchElementException.class, () -> service.deleteCredentials("1"));
        verify(repo).existsById("1");
    }
}