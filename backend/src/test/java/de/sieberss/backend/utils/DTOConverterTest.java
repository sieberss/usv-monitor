package de.sieberss.backend.utils;

import de.sieberss.backend.model.*;
import de.sieberss.backend.repo.UpsRepo;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class DTOConverterTest {

    private final UpsRepo upsRepo = mock(UpsRepo.class);
    private final EncryptionService encryptionService = mock(EncryptionService.class);
    private final DTOConverter converter = new DTOConverter(upsRepo, encryptionService);

    @Test
    void getServerFromDTO_shouldReturnNull_whenDTOIsNull() {
        assertNull(converter.getServerFromDTO(null));
    }

    @Test
    void getServerFromDTO_shouldEncryptCredentials_andGetUpsFromDatabase_whenIdExists() {
        Ups ups
                = new Ups("2", "ups", "localhost", "c");
        CredentialsWithoutEncryption decrypted
                = new CredentialsWithoutEncryption("1", "user", "secret", true);
        Credentials encrypted
                = new Credentials("1", "user", "klkjterer", true);
        ServerDTO dto
                = new ServerDTO("44", "server", "1.1.1.1", decrypted, "2");
        Server expected
                = new Server("44", "server", "1.1.1.1", encrypted, ups);
        when(upsRepo.findById("2"))
                .thenReturn(Optional.of(ups));
        when(encryptionService.encryptCredentials(decrypted))
                .thenReturn(encrypted);
        // execute method
        Server actual = converter.getServerFromDTO(dto);
        assertEquals(actual, expected);
        verify(upsRepo).findById("2");
        verify(encryptionService).encryptCredentials(decrypted);
    }

    @Test
    void getServerFromDTO_shouldEncryptCredentials_andSetUpsNull_whenIdDoesNotExist() {
        CredentialsWithoutEncryption decrypted
                = new CredentialsWithoutEncryption("1", "user", "secret", true);
        Credentials encrypted
                = new Credentials("1", "user", "klkjterer", true);
        ServerDTO dto
                = new ServerDTO("44", "server", "1.1.1.1", decrypted, "2");
        Server expected
                = new Server("44", "server", "1.1.1.1", encrypted, null);
        when(upsRepo.findById("2"))
                .thenReturn(Optional.empty());
        when(encryptionService.encryptCredentials(decrypted))
                .thenReturn(encrypted);
        // execute method
        Server actual = converter.getServerFromDTO(dto);
        assertEquals(actual, expected);
        verify(upsRepo).findById("2");
        verify(encryptionService).encryptCredentials(decrypted);
    }

    @Test
    void getDTOFromServer_shouldDecryptCredentials_andSetUpsId_whenUpsIsNotNull() {
        Ups ups
                = new Ups("2", "ups", "localhost", "c");
        CredentialsWithoutEncryption decrypted
                = new CredentialsWithoutEncryption("1", "user", "secret", true);
        Credentials encrypted
                = new Credentials("1", "user", "klkjterer", true);
        Server server
                = new Server("44", "server", "1.1.1.1", encrypted, ups);
        ServerDTO dto
                = new ServerDTO("44", "server", "1.1.1.1", decrypted, "2");
        when(encryptionService.decryptCredentials(encrypted))
                .thenReturn(decrypted);
        // execute method
        ServerDTO actual = converter.getDTOFromServer(server);
        assertEquals(actual, dto);
        verify(encryptionService).decryptCredentials(encrypted);
    }
    @Test
    void getDTOFromServer_shouldDecryptCredentials_andSetUpsIdEmpty_whenUpsIsNull() {
        CredentialsWithoutEncryption decrypted
                = new CredentialsWithoutEncryption("1", "user", "secret", true);
        Credentials encrypted
                = new Credentials("1", "user", "klkjterer", true);
        Server server
                = new Server("44", "server", "1.1.1.1", encrypted, null);
        ServerDTO dto
                = new ServerDTO("44", "server", "1.1.1.1", decrypted, "");
        when(encryptionService.decryptCredentials(encrypted))
                .thenReturn(decrypted);
        // execute method
        ServerDTO actual = converter.getDTOFromServer(server);
        assertEquals(actual, dto);
        verify(encryptionService).decryptCredentials(encrypted);
    }

    @Test
    void getDTOFromServer_shouldReturnNull_whenDTOIsNull() {
        assertNull(converter.getDTOFromServer(null));
    }
}