package de.sieberss.backend.utils;

import de.sieberss.backend.model.*;
import de.sieberss.backend.repo.UpsRepo;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class DTOConverterTest {

    private final UpsRepo upsRepo = mock(UpsRepo.class);
    private final ServerDTOConverter converter = new ServerDTOConverter(upsRepo);

    @Test
    void getServerFromDTO_shouldReturnNull_whenDTOIsNull() {
        assertNull(converter.getServerFromDTO(null));
    }

    @Test
    void getServerFromDTO_shouldEncryptCredentials_andGetUpsFromDatabase_whenIdExists() {
        Ups ups
                = new Ups("2", "ups", "localhost", "c");
        CredentialsDTO decrypted
                = new CredentialsDTO("1", "user", "secret", true);
        Credentials encrypted
                = new Credentials("1", "user", EncryptionService.encryptPassword("secret"), true);
        ServerDTO dto
                = new ServerDTO("44", "server", "1.1.1.1", decrypted, "2", 180);
        Server expected
                = new Server("44", "server", "1.1.1.1", encrypted, ups, 180);
        when(upsRepo.findById("2"))
                .thenReturn(Optional.of(ups));
        // execute method
        Server actual = converter.getServerFromDTO(dto);
        assertEquals(actual, expected);
        verify(upsRepo).findById("2");
    }

    @Test
    void getServerFromDTO_shouldEncryptCredentials_andSetUpsNull_whenIdDoesNotExist() {
        CredentialsDTO decrypted
                = new CredentialsDTO("1", "user", "secret", true);
        Credentials encrypted
                = new Credentials("1", "user", EncryptionService.encryptPassword("secret"), true);
        ServerDTO dto
                = new ServerDTO("44", "server", "1.1.1.1", decrypted, "2", 180);
        Server expected
                = new Server("44", "server", "1.1.1.1", encrypted, null, 180);
        when(upsRepo.findById("2"))
                .thenReturn(Optional.empty());
        // execute method
        Server actual = converter.getServerFromDTO(dto);
        assertEquals(actual, expected);
        verify(upsRepo).findById("2");
    }

    @Test
    void getDTOFromServer_shouldDecryptCredentials_andSetUpsId_whenUpsIsNotNull() {
        Ups ups
                = new Ups("2", "ups", "localhost", "c");
        CredentialsDTO decrypted
                = new CredentialsDTO("1", "user", "secret", true);
        Credentials encrypted
                = new Credentials("1", "user", EncryptionService.encryptPassword("secret"), true);
        Server server
                = new Server("44", "server", "1.1.1.1", encrypted, ups, 180);
        ServerDTO dto
                = new ServerDTO("44", "server", "1.1.1.1", decrypted, "2", 180);
        // execute method
        ServerDTO actual = converter.getDTOFromServer(server);
        assertEquals(actual, dto);
    }

    @Test
    void getDTOFromServer_shouldDecryptCredentials_andSetUpsIdEmpty_whenUpsIsNull() {
        CredentialsDTO decrypted
                = new CredentialsDTO("1", "user", "secret", true);
        Credentials encrypted
                = new Credentials("1", "user", EncryptionService.encryptPassword("secret"), true);
        Server server
                = new Server("44", "server", "1.1.1.1", encrypted, null, 180);
        ServerDTO dto
                = new ServerDTO("44", "server", "1.1.1.1", decrypted, "", 180);
        // execute method
        ServerDTO actual = converter.getDTOFromServer(server);
        assertEquals(actual, dto);
    }

    @Test
    void getDTOFromServer_shouldReturnNull_whenDTOIsNull() {
        assertNull(converter.getDTOFromServer(null));
    }
}