package de.sieberss.backend.service;

import de.sieberss.backend.model.Server;
import de.sieberss.backend.model.ServerDTO;
import de.sieberss.backend.model.Ups;
import de.sieberss.backend.repo.ServerRepo;
import de.sieberss.backend.repo.UpsRepo;
import de.sieberss.backend.utils.DTOConverter;
import de.sieberss.backend.utils.EncryptionService;
import de.sieberss.backend.utils.IdService;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ServerServiceTest {

private final ServerRepo repo = mock(ServerRepo.class);
private final IdService idService = mock(IdService.class);
private final UpsRepo upsRepo = mock(UpsRepo.class);
private final EncryptionService encryptionService = mock(EncryptionService.class);
private final DTOConverter converter = new DTOConverter(upsRepo, encryptionService);
private final ServerService service = new ServerService(repo, idService, converter);

    @Test
    void getServerListshouldReturnEmptyList_whenRepoIsEmpty() {
        List<ServerDTO> expected = List.of();
        final List<ServerDTO> actual = service.getServerDTOList();
        assertEquals(expected, actual);
        verify(repo).findAll();
    }

    @Test
    void getServerListShouldReturnList_whenRepoIsNotEmpty() {
        Ups ups = new Ups("1", "Test-UPS", "192.168.1.1", "");
    }

    @Test
    void getServerById() {
    }

    @Test
    void createServer() {
    }

    @Test
    void updateServer() {
    }

    @Test
    void deleteServer() {
    }
}