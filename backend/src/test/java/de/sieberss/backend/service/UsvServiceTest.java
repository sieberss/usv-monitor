package de.sieberss.backend.service;

import de.sieberss.backend.model.Usv;
import de.sieberss.backend.model.UsvRepo;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

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

    @Test
    void getUsvById_shouldReturnObject_whenIdExists(){
        Usv expected = new Usv("1", "Test-USV", "192.168.1.1", "");
        Mockito.when(repo.findById("1")).thenReturn(Optional.of(expected));
        Usv actual = service.getUsvById("1");
        assertEquals(expected, actual);
        Mockito.verify(repo).findById("1");
    }

    @Test
    void getUsvById_shouldReturnErrorMessage_whenIdDoesNotExist(){
        Mockito.when(repo.findById("1")).thenReturn(Optional.empty());
        assertThrows(NoSuchElementException.class, () -> service.getUsvById("1"));
        Mockito.verify(repo).findById("1");
    }
}