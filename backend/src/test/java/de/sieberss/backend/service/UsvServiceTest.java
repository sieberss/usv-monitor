package de.sieberss.backend.service;

import de.sieberss.backend.model.Usv;
import de.sieberss.backend.model.UsvRepo;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class UsvServiceTest {

    private final UsvRepo repo = Mockito.mock(UsvRepo.class);
    private final UsvService service = new UsvService(repo);


    @Test
    void getUsvList_shouldReturnEmptyList_whenRepoIsEmpty() {
        List<Usv> expected = Collections.emptyList();
        Mockito.when(repo.findAll()).thenReturn(expected);
        List<Usv> actual = service.getUsvList();
        assertEquals(expected, actual);
        Mockito.verify(repo).findAll();
    }

    @Test
    void getUsvList_shouldReturnContent_whenRepoIsFilled() {
        List<Usv> expected = List.of(new Usv("1", "Test-USV", "192.168.1.1", ""));
        Mockito.when(repo.findAll()).thenReturn(expected);
        List<Usv> actual = service.getUsvList();
        assertEquals(expected, actual);
        Mockito.verify(repo).findAll();
    }
}