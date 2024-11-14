package de.sieberss.backend.service;

import de.sieberss.backend.model.Credentials;
import de.sieberss.backend.model.CredentialsDTO;
import de.sieberss.backend.repo.CredentialsRepo;
import de.sieberss.backend.utils.EncryptionService;
import de.sieberss.backend.utils.IdService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class CredentialsServiceTest {

    private final CredentialsRepo repo = mock(CredentialsRepo.class);
    private final IdService idService = mock(IdService.class);
    private final CredentialsService service = new CredentialsService(repo, idService);

    @BeforeAll
    static void setUp () throws Exception {
        EncryptionService.setTestKey();
    }

    @Test
    void getCredentialsList_shouldReturnEmptyList_whenDatabaseIsEmpty() {
        when(repo.findAll()).thenReturn(List.of());
        assertEquals(List.of(), service.getCredentialsList());
        verify(repo).findAll();
    }

    @Test
    void getCredentialsList_shouldReturnContent_whenRepoIsFilled() {
        String password = "secret";
        CredentialsDTO unencrypted = new CredentialsDTO("1", "user", password, true);
        Credentials encrypted = new Credentials("1", "user", EncryptionService.encryptPassword(password), true);
        when(repo.findAll()).thenReturn(List.of(encrypted));
        // execute tested method
        assertEquals(List.of(unencrypted), service.getCredentialsList());
        verify(repo).findAll();
    }

    @Test
    void getCredentialsList_shouldReturnAppUsers_withEncryptedPassword(){
        Credentials appUser = new Credentials("1", "APP/user", "jkjglrtrtretr", false);
        CredentialsDTO expected = new CredentialsDTO("1", "APP/user", "jkjglrtrtretr", false);
        when(repo.findAll()).thenReturn(List.of(appUser));
        // execute tested method
        assertEquals(List.of(expected), service.getCredentialsList());
        verify(repo).findAll();
    }

    @Test
    void getCredentialsById_shouldReturnObject_whenIdExists() {
        String password = "secret";
        CredentialsDTO unencrypted = new CredentialsDTO("1", "user", password, true);
        Credentials encrypted = new Credentials("1", "user", EncryptionService.encryptPassword(password), true);
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
        when(idService.generateId()).thenReturn("1");
        CredentialsDTO submitted = new CredentialsDTO("", "user", "pass", true);
        CredentialsDTO expected = new CredentialsDTO("1", "user", "pass", true);
        Credentials encrypted = new Credentials("1", "user", EncryptionService.encryptPassword("pass"), true);
        when(repo.save(encrypted)).thenReturn(encrypted);
        // execute tested method
        CredentialsDTO actual = service.createCredentials(submitted);
        assertEquals(expected, actual);
        verify(repo).save(encrypted);
        verify(idService).generateId();
    }

    @Test
    void updateCredentials_shouldReturnUpdatedObject_whenIdExists() {
        CredentialsDTO submitted = new CredentialsDTO("null", "user", "pass", true);
        CredentialsDTO expected = new CredentialsDTO("1", "user", "pass", true);
        Credentials encrypted = new Credentials("1", "user", EncryptionService.encryptPassword("pass"), true);
        when(repo.existsById("1")).thenReturn(true);
        when(repo.save(encrypted)).thenReturn(encrypted);
        // execute tested method
        CredentialsDTO actual = service.updateCredentials("1", submitted);
        assertEquals(expected, actual);
        verify(repo).existsById("1");
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