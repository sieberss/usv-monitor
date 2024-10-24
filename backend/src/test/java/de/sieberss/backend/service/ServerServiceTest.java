package de.sieberss.backend.service;

import de.sieberss.backend.model.*;
import de.sieberss.backend.repo.ServerRepo;
import de.sieberss.backend.utils.DTOConverter;
import de.sieberss.backend.utils.IdService;
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
private final ServerService service = new ServerService(repo, idService, converter);

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
                = new Server("22", "server", "1.1.1.1", encrypted, ups);
        ServerDTO dto
                = new ServerDTO("22", "server", "1.1.1.1", decrypted, "1");
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
                = new Server("22", "server", "1.1.1.1", encrypted, ups);
        ServerDTO expected
                = new ServerDTO("22", "server", "1.1.1.1", decrypted, "1");
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
        // requested Id does not exist by default in mocked db, so no when-clause necessary
        assertThrows(NoSuchElementException.class, () -> service.getServerDTOById("22"));
        verify(repo).findById("22");
    }

    @Test
    void createServer_shouldCreateServer_withSubmittedData() {
        Ups ups = new Ups("1", "Test-UPS", "192.168.1.1", "");
        Credentials encrypted = new Credentials("8", "user", "UHJHJK", true);
        CredentialsWithoutEncryption decrypted = new CredentialsWithoutEncryption("8", "user", "pass", true);
        ServerDTO submitted
                = new ServerDTO(null, "server", "1.1.1.1", decrypted, "1");
        Server server
                = new Server("22", "server", "1.1.1.1", encrypted, ups);
        ServerDTO expected
                = new ServerDTO("22", "server", "1.1.1.1", decrypted, "1");
        when(idService.generateId()).thenReturn("22");
        when(converter.getServerFromDTO(expected)).thenReturn(server);
        when(repo.save(server)).thenReturn(server);
        // execute method
        ServerDTO actual = service.createServer(submitted);
        verify(repo).save(server);
        verify(idService).generateId();
        assertEquals(expected, actual);
    }

    @Test
    void updateServer_shouldUpdateServerWithSubmittedData_ifIdExists() {
        Ups ups = new Ups("1", "Test-UPS", "192.168.1.1", "");
        Credentials encrypted = new Credentials("8", "user", "UHJHJK", true);
        CredentialsWithoutEncryption decrypted = new CredentialsWithoutEncryption("8", "user", "pass", true);
        ServerDTO submitted
                = new ServerDTO(null, "server", "1.1.1.1", decrypted, "1");
        Server UpdatedServer
                = new Server("22", "server", "1.1.1.1", encrypted, ups);
        ServerDTO expected
                = new ServerDTO("22", "server", "1.1.1.1", decrypted, "1");
        when(repo.existsById("22")).thenReturn(true);
        when(converter.getServerFromDTO(expected)).thenReturn(UpdatedServer);
        when(repo.save(UpdatedServer)).thenReturn(UpdatedServer);
        // execute method
        ServerDTO actual = service.updateServer("22", submitted);
        verify(repo).existsById("22");
        verify(converter).getServerFromDTO(expected);
        verify(repo).save(UpdatedServer);
        assertEquals(expected, actual);
    }


    @Test
    void updateServer_shouldThrowNoSuchElementException_whenIdDoesNotExist() {
        CredentialsWithoutEncryption decrypted = new CredentialsWithoutEncryption("8", "user", "pass", true);
        ServerDTO submitted
                = new ServerDTO("", "server", "1.1.1.1", decrypted, "1");
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
}