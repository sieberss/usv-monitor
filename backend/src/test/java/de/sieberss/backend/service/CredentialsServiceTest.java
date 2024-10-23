package de.sieberss.backend.service;

import de.sieberss.backend.model.Credentials;
import de.sieberss.backend.model.CredentialsWithoutEncryption;
import de.sieberss.backend.repo.CredentialsRepo;
import de.sieberss.backend.utils.EncryptionService;
import de.sieberss.backend.utils.IdService;
import org.junit.jupiter.api.Test;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CredentialsServiceTest {

    private final CredentialsRepo repo = mock(CredentialsRepo.class);
    private final IdService idService = mock(IdService.class);
    private final EncryptionService encryptionService = mock(EncryptionService.class);
    private final CredentialsService service = new CredentialsService(repo, idService, encryptionService);

    @Test
    void getCredentialsList_shouldReturnEmptyList_whenDatabaseIsEmpty() {
        when(repo.findAll()).thenReturn(List.of());
        assertEquals(List.of(), service.getCredentialsList());
        verify(repo).findAll();
    }

    @Test
    void getCredentialsList_shouldReturnContent_whenRepoIsFilled() {
        CredentialsWithoutEncryption unencrypted = new CredentialsWithoutEncryption("1", "user", "secret", true);
        Credentials encrypted = new Credentials("1", "user", "*****", true);
        when(encryptionService.decryptCredentials(encrypted)).thenReturn(unencrypted);
        when(repo.findAll()).thenReturn(List.of(encrypted));
        // execute tested method
        assertEquals(List.of(unencrypted), service.getCredentialsList());
        verify(repo).findAll();
        verify(encryptionService).decryptCredentials(encrypted);
    }

    @Test
    void getCredentialsById_shouldReturnObject_whenIdExists() {
        CredentialsWithoutEncryption unencrypted = new CredentialsWithoutEncryption("1", "user", "secret", true);
        Credentials encrypted = new Credentials("1", "user", "*****", true);
        when(encryptionService.decryptCredentials(encrypted)).thenReturn(unencrypted);
        when(repo.findById("1")).thenReturn(Optional.of(encrypted));
        // execute tested method
        CredentialsWithoutEncryption actual = service.getCredentialsById("1");
        assertEquals(unencrypted, actual);
        verify(repo).findById("1");
        verify(encryptionService).decryptCredentials(encrypted);
    }


    @Test
    void getCredentialsById_shouldThrowNoSuchElementException_whenIdDoesNotExist() {
        when(repo.findById("1")).thenReturn(Optional.empty());
        assertThrows(NoSuchElementException.class, () -> service.getCredentialsById("1"));
        verify(repo).findById("1");
    }

    @Test
    void createCredentials_shouldReturn_ObjectWithNewIdAndSubmittedData() {
        when(idService.generateId()).thenReturn("1");
        CredentialsWithoutEncryption submitted = new CredentialsWithoutEncryption("", "user", "pass", true);
        CredentialsWithoutEncryption expected = new CredentialsWithoutEncryption("1", "user", "pass", true);
        Credentials encrypted = new Credentials("1", "user", "*****", true);
        when(encryptionService.encryptCredentials(expected)).thenReturn(encrypted);
        when(repo.save(encrypted)).thenReturn(encrypted);
        // execute tested method
        CredentialsWithoutEncryption actual = service.createCredentials(submitted);
        assertEquals(expected, actual);
        verify(repo).save(encrypted);
        verify(idService).generateId();
        verify(encryptionService).encryptCredentials(expected);
    }

    @Test
    void updateCredentials_shouldReturnUpdatedObject_whenIdExists() {
        CredentialsWithoutEncryption submitted = new CredentialsWithoutEncryption("null", "user", "pass", true);
        CredentialsWithoutEncryption expected = new CredentialsWithoutEncryption("1", "user", "pass", true);
        Credentials encrypted = new Credentials("1", "user", "*****", true);
        when(repo.existsById("1")).thenReturn(true);
        when(encryptionService.encryptCredentials(expected)).thenReturn(encrypted);
        when(repo.save(encrypted)).thenReturn(encrypted);
        // execute tested method
        CredentialsWithoutEncryption actual = service.updateCredentials("1", submitted);
        assertEquals(expected, actual);
        verify(repo).existsById("1");
        verify(encryptionService).encryptCredentials(expected);
        verify(repo).save(encrypted);
    }

    @Test
    void updateCredentials_shouldThrowNoSuchElementException_whenIdDoesNotExist() {
        CredentialsWithoutEncryption submitted = new CredentialsWithoutEncryption("null", "user", "pass", true);
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