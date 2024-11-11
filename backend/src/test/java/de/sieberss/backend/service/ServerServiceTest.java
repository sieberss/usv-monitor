package de.sieberss.backend.service;

import de.sieberss.backend.model.*;
import de.sieberss.backend.repo.CredentialsRepo;
import de.sieberss.backend.repo.ServerRepo;
import de.sieberss.backend.utils.DTOConverter;
import de.sieberss.backend.utils.EncryptionService;
import de.sieberss.backend.utils.IdService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ServerServiceTest {

private final ServerRepo repo = mock(ServerRepo.class);
private final IdService idService = mock(IdService.class);
private final DTOConverter converter = mock (DTOConverter.class);
private final CredentialsRepo credentialsRepo = mock(CredentialsRepo.class);
private final EncryptionService encryptionService = mock(EncryptionService.class);
private final CredentialsService credentialsService = new CredentialsService(credentialsRepo, idService, encryptionService);
private final ServerService service = new ServerService(repo, idService, converter, credentialsService);


    @Test
    void getServerDTOList_shouldReturnEmptyList_whenRepoIsEmpty() {
        List<ServerDTO> expected = List.of();
        final List<ServerDTO> actual = service.getServerDTOList();
        assertEquals(expected, actual);
        verify(repo).findAll();
    }

    @Test
    void getServerDTOListShouldReturnList_whenRepoIsNotEmpty() {
        Ups ups = new Ups("1", "Test-UPS", "192.168.1.1", "");
        Credentials encrypted = new Credentials("8", "user", "UHJHJK", true);
        CredentialsWithoutEncryption decrypted = new CredentialsWithoutEncryption("8", "user", "pass", true);
        Server server
                = new Server("22", "server", "1.1.1.1", encrypted, ups, 180);
        ServerDTO dto
                = new ServerDTO("22", "server", "1.1.1.1", decrypted, "1", 180);
        when(repo.findAll()).thenReturn(List.of(server));
        when(converter.getDTOFromServer(server)).thenReturn(dto);
        //execute method
        List<ServerDTO> actual = service.getServerDTOList();
        verify(repo).findAll();
        verify(converter).getDTOFromServer(server);
        assertEquals(List.of(dto), actual);
    }

    @Test
    void getServerDTOById_shouldReturnServerDTO_whenIdExists() {
        Ups ups = new Ups("1", "Test-UPS", "192.168.1.1", "");
        Credentials encrypted = new Credentials("8", "user", "UHJHJK", true);
        CredentialsWithoutEncryption decrypted = new CredentialsWithoutEncryption("8", "user", "pass", true);
        Server server
                = new Server("22", "server", "1.1.1.1", encrypted, ups, 180);
        ServerDTO expected
                = new ServerDTO("22", "server", "1.1.1.1", decrypted, "1", 180);
        when(repo.findById("22")).thenReturn(Optional.of(server));
        when(converter.getDTOFromServer(server)).thenReturn(expected);
        // execute method
        ServerDTO actual = service.getServerDTOById("22");
        verify(repo).findById("22");
        verify(converter).getDTOFromServer(server);
        assertEquals(expected, actual);
    }

    @Test
    void getServerDTOById_shouldThrowNoSuchElementException_whenIdDoesNotExist() {
        // requested ID does not exist by default in mocked db, so no when-clause necessary
        assertThrows(NoSuchElementException.class, () -> service.getServerDTOById("22"));
        verify(repo).findById("22");
    }

    @Test
    void createServer_shouldCreateServer_withSubmittedData() {
        Ups ups = new Ups("1", "Test-UPS", "192.168.1.1", "");
        Credentials encrypted = new Credentials("8", "user", EncryptionService.encryptPassword("pass"), true);
        CredentialsWithoutEncryption decrypted = new CredentialsWithoutEncryption("8", "user", "pass", true);
        ServerDTO submitted
                = new ServerDTO(null, "server", "1.1.1.1", decrypted, "1", 180);
        Server server
                = new Server("22", "server", "1.1.1.1", encrypted, ups, 180);
        ServerDTO expected
                = new ServerDTO("22", "server", "1.1.1.1", decrypted, "1", 180);
        when(idService.generateId()).thenReturn("22");
        when(converter.getServerFromDTO(expected)).thenReturn(server);
        when(converter.getDTOFromServer(server)).thenReturn(expected);
        when(repo.save(server)).thenReturn(server);
        // execute method
        ServerDTO actual = service.createServer(submitted);
        verify(repo).save(server);
        verify(idService).generateId();
        verify(converter).getServerFromDTO(expected);
        assertEquals(expected, actual);
    }

    @Test
    void createServer_shouldCreateServerwithSubmittedData_butEmptyUpsId_whenUpsIdNotInDatabase() {
        Credentials encrypted = new Credentials("8", "user", "UHJHJK", true);
        CredentialsWithoutEncryption decrypted = new CredentialsWithoutEncryption("8", "user", "pass", true);
        ServerDTO submitted
                = new ServerDTO(null, "server", "1.1.1.1", decrypted, "1", 180);
        ServerDTO completed
                = new ServerDTO("22", "server", "1.1.1.1", decrypted, "1", 180);
        Server server
                = new Server("22", "server", "1.1.1.1", encrypted, null, 180);
        ServerDTO expected
                = new ServerDTO("22", "server", "1.1.1.1", decrypted, "", 180);
        when(idService.generateId()).thenReturn("22");
        when(converter.getServerFromDTO(completed)).thenReturn(server);
        when(converter.getDTOFromServer(server)).thenReturn(expected);
        when(repo.save(server)).thenReturn(server);
        // execute method
        ServerDTO actual = service.createServer(submitted);
        verify(repo).save(server);
        verify(idService).generateId();
        verify(converter).getServerFromDTO(completed);
        assertEquals(expected, actual);
    }

    @Test
    void updateServer_shouldUpdateServerWithSubmittedData_ifIdExists() {
        Ups ups = new Ups("1", "Test-UPS", "192.168.1.1", "");
        Credentials encrypted = new Credentials("8", "user", "UHJHJK", true);
        CredentialsWithoutEncryption decrypted = new CredentialsWithoutEncryption("8", "user", "pass", true);
        ServerDTO submitted
                = new ServerDTO(null, "server", "1.1.1.1", decrypted, "1", 180);
        Server updatedServer
                = new Server("22", "server", "1.1.1.1", encrypted, ups, 180);
        ServerDTO expected
                = new ServerDTO("22", "server", "1.1.1.1", decrypted, "1", 180);
        when(repo.existsById("22")).thenReturn(true);
        when(converter.getServerFromDTO(expected)).thenReturn(updatedServer);
        when(converter.getDTOFromServer(updatedServer)).thenReturn(expected);
        when(repo.save(updatedServer)).thenReturn(updatedServer);
        // execute method
        ServerDTO actual = service.updateServer("22", submitted);
        verify(repo).existsById("22");
        verify(converter).getServerFromDTO(expected);
        verify(converter).getDTOFromServer(updatedServer);
        verify(repo).save(updatedServer);
        assertEquals(expected, actual);
    }

    @Test
    void updateServer_shouldUpdateServerWithSubmittedData_butEmptyUpsId_ifIdExists_butNotUpsId() {
        Credentials encrypted = new Credentials("8", "user", "UHJHJK", true);
        CredentialsWithoutEncryption decrypted = new CredentialsWithoutEncryption("8", "user", "pass", true);
        ServerDTO submitted
                = new ServerDTO(null, "server", "1.1.1.1", decrypted, "1", 180);
        ServerDTO completed
                = new ServerDTO("22", "server", "1.1.1.1", decrypted, "1", 180);
        Server updatedServer
                = new Server("22", "server", "1.1.1.1", encrypted, null, 180);
        ServerDTO expected
                = new ServerDTO("22", "server", "1.1.1.1", decrypted, "", 180);
        when(repo.existsById("22")).thenReturn(true);
        when(converter.getServerFromDTO(completed)).thenReturn(updatedServer);
        when(converter.getDTOFromServer(updatedServer)).thenReturn(expected);
        when(repo.save(updatedServer)).thenReturn(updatedServer);
        // execute method
        ServerDTO actual = service.updateServer("22", submitted);
        verify(repo).existsById("22");
        verify(converter).getServerFromDTO(completed);
        verify(converter).getDTOFromServer(updatedServer);
        verify(repo).save(updatedServer);
        assertEquals(expected, actual);
    }

    @Test
    void updateServer_shouldThrowNoSuchElementException_whenIdDoesNotExist() {
        CredentialsWithoutEncryption decrypted = new CredentialsWithoutEncryption("8", "user", "pass", true);
        ServerDTO submitted
                = new ServerDTO("", "server", "1.1.1.1", decrypted, "1", 180);
        // execute method
        assertThrows(NoSuchElementException.class, () -> service.updateServer("22", submitted));
        verify(repo).existsById("22");
    }

    @Test
    void deleteServer_shouldThrowNoSuchElementException_whenIdDoesNotExist() {
        assertThrows(NoSuchElementException.class, () -> service.deleteServer("22"));
        verify(repo).existsById("22");
    }

    @Test
    void deleteServer_shouldDeleteServer_whenIdExists(){
        when(repo.existsById("22")).thenReturn(true);
        service.deleteServer("22");
        verify(repo).existsById("22");
        verify(repo).deleteById("22");
    }

    @Test
    void createServerWithNewLocalCredentials_shouldCreateServer_withSubmittedData() {
        Ups ups = new Ups("1", "Test-UPS", "192.168.1.1", "");
        Credentials encrypted = new Credentials("8", "user", EncryptionService.encryptPassword("pass"), false);
        CredentialsWithoutEncryption decrypted = new CredentialsWithoutEncryption("8", "user", "pass", false);
        ServerDTOWithoutCredentialsId submitted
                = new ServerDTOWithoutCredentialsId(null, "server", "1.1.1.1", "user", "pass","1", 180);
        Server server
                = new Server("22", "server", "1.1.1.1", encrypted, ups, 180);
        ServerDTO expected
                = new ServerDTO("22", "server", "1.1.1.1", decrypted, "1", 180);
        when(idService.generateId()).thenReturn("8","22");
        when(credentialsRepo.save(encrypted)).thenReturn(encrypted);
        when(converter.getServerFromDTO(expected)).thenReturn(server);
        when(converter.getDTOFromServer(server)).thenReturn(expected);
        when(repo.save(server)).thenReturn(server);
        // execute method
        ServerDTO actual = service.createServerWithNewLocalCredentials(submitted);
        verify(repo).save(server);
        verify(idService, times(2)).generateId();
        verify(credentialsRepo).save(encrypted);
        verify(converter).getServerFromDTO(expected);
        verify(converter).getDTOFromServer(server);
        assertEquals(expected, actual);
    }

    @Test
    void createServerWithNewLocalCredentials_shouldThrowIllegalArgumentException_whenUsernameEmpty() {
        ServerDTOWithoutCredentialsId submitted
                = new ServerDTOWithoutCredentialsId(null, "server", "1.1.1.1", "", "pass","1", 180);
        // execute method
        assertThrows(IllegalArgumentException.class, () -> service.createServerWithNewLocalCredentials(submitted));
    }

    @Test
    void createServerWithNewLocalCredentials_shouldThrowIllegalArgumentException_whenUsernameNull() {
        ServerDTOWithoutCredentialsId submitted
                = new ServerDTOWithoutCredentialsId(null, "server", "1.1.1.1", null, "pass","1", 180);
        // execute method
        assertThrows(IllegalArgumentException.class, () -> service.createServerWithNewLocalCredentials(submitted));
    }

    @Test
    void createServerWithNewLocalCredentials_shouldThrowIllegalArgumentException_whenPasswordEmpty() {
        ServerDTOWithoutCredentialsId submitted
                = new ServerDTOWithoutCredentialsId(null, "server", "1.1.1.1", "user", "","1", 180);
        // execute method
        assertThrows(IllegalArgumentException.class, () -> service.createServerWithNewLocalCredentials(submitted));
    }

    @Test
    void createServerWithNewLocalCredentials_shouldThrowIllegalArgumentException_whenPasswordNull() {
        ServerDTOWithoutCredentialsId submitted
                = new ServerDTOWithoutCredentialsId(null, "server", "1.1.1.1", "user", null,"1", 180);
        // execute method
        assertThrows(IllegalArgumentException.class, () -> service.createServerWithNewLocalCredentials(submitted));
    }

    @Test
    void updateServerWithNewLocalCredentials_shouldUpdateData_WithNewLocalCredentials() {
        Ups ups = new Ups("1", "Test-UPS", "192.168.1.1", "");
        Credentials encrypted = new Credentials("8", "user", EncryptionService.encryptPassword("pass"), false);
        CredentialsWithoutEncryption decrypted = new CredentialsWithoutEncryption("8", "user", "pass", false);
        ServerDTOWithoutCredentialsId submitted
                = new ServerDTOWithoutCredentialsId(null, "server", "1.1.1.1", "user", "pass","1", 180);
        Server server
                = new Server("22", "server", "1.1.1.1", encrypted, ups, 180);
        ServerDTO expected
                = new ServerDTO("22", "server", "1.1.1.1", decrypted, "1", 180);
        when(repo.existsById("22")).thenReturn(true);
        when(idService.generateId()).thenReturn("8");
        when(credentialsRepo.save(encrypted)).thenReturn(encrypted);
        when(converter.getServerFromDTO(expected)).thenReturn(server);
        when(converter.getDTOFromServer(server)).thenReturn(expected);
        when(repo.save(server)).thenReturn(server);
        // execute method
        ServerDTO actual = service.updateServerWithNewLocalCredentials("22", submitted);
        verify(repo).existsById("22");
        verify(repo).save(server);
        verify(idService).generateId();
        verify(credentialsRepo).save(encrypted);
        verify(converter).getServerFromDTO(expected);
        verify(converter).getDTOFromServer(server);
        assertEquals(expected, actual);
    }

    @Test
    void updateServerWithNewLocalCredentials_shouldThrowIllegalArgumentException_whenUsernameEmpty() {
        ServerDTOWithoutCredentialsId submitted
                = new ServerDTOWithoutCredentialsId(null, "server", "1.1.1.1", "", "pass","1", 180);
        // execute method
        assertThrows(IllegalArgumentException.class, () -> service.updateServerWithNewLocalCredentials("1", submitted));
    }

    @Test
    void updateServerWithNewLocalCredentials_shouldThrowIllegalArgumentException_whenUsernameNull() {
        ServerDTOWithoutCredentialsId submitted
                = new ServerDTOWithoutCredentialsId(null, "server", "1.1.1.1", null, "pass","1", 180);
        // execute method
        assertThrows(IllegalArgumentException.class, () -> service.updateServerWithNewLocalCredentials("1", submitted));
    }

    @Test
    void updateServerWithNewLocalCredentials_shouldThrowIllegalArgumentException_whenPasswordEmpty() {
        ServerDTOWithoutCredentialsId submitted
                = new ServerDTOWithoutCredentialsId(null, "server", "1.1.1.1", "user", "","1", 180);
        // execute method
        assertThrows(IllegalArgumentException.class, () -> service.updateServerWithNewLocalCredentials("1", submitted));
    }

    @Test
    void updateServerWithNewLocalCredentials_shouldThrowIllegalArgumentException_whenPasswordNull() {
        ServerDTOWithoutCredentialsId submitted
                = new ServerDTOWithoutCredentialsId(null, "server", "1.1.1.1", "user", null,"1", 180);
        // execute method
        assertThrows(IllegalArgumentException.class, () -> service.updateServerWithNewLocalCredentials("!", submitted));
    }
}