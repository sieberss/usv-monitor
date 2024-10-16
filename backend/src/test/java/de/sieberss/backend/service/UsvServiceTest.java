package de.sieberss.backend.service;

import de.sieberss.backend.model.Usv;
import de.sieberss.backend.model.UsvRepo;
import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.*;

import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class UsvServiceTest {

    private final UsvRepo repo = mock(UsvRepo.class);
    private final IdService idService = mock(IdService.class);
    private final UsvService service = new UsvService(repo, idService);


    @Test
    void getUsvList_shouldReturnEmptyList_whenRepoIsEmpty() {
        List<Usv> expected = Collections.emptyList();
        when(repo.findAll()).thenReturn(expected);
        List<Usv> actual = service.getUsvList();
        assertEquals(expected, actual);
        verify(repo).findAll();
    }

    @Test
    void getUsvList_shouldReturnContent_whenRepoIsFilled() {
        List<Usv> expected = List.of(new Usv("1", "Test-USV", "192.168.1.1", ""));
        when(repo.findAll()).thenReturn(expected);
        List<Usv> actual = service.getUsvList();
        assertEquals(expected, actual);
        verify(repo).findAll();
    }

    @Test
    void getUsvById_shouldReturnObject_whenIdExists(){
        Usv expected = new Usv("1", "Test-USV", "192.168.1.1", "");
        when(repo.findById("1")).thenReturn(Optional.of(expected));
        Usv actual = service.getUsvById("1");
        assertEquals(expected, actual);
        verify(repo).findById("1");
    }

    @Test
    void getUsvById_shouldThrowNoSuchElementException_whenIdDoesNotExist(){
        when(repo.findById("1")).thenReturn(Optional.empty());
        assertThrows(NoSuchElementException.class, () -> service.getUsvById("1"));
        verify(repo).findById("1");
    }

    @Test
    void createUsv_shouldCreateUsvWithSubmittedData() {
        when(idService.generateId()).thenReturn("abc");
        Usv submitted = new Usv("1", "Test", "192.168.1.1", "");
        Usv expected = new Usv("abc", "Test", "192.168.1.1", "");
        when(repo.save(expected)).thenReturn(expected);
        Usv actual = service.createUsv(submitted);
        verify(repo).save(expected);
        assertEquals(expected, actual);
    }

    @Test
    void updateUsv_shouldUpdateUsvWithSubmittedData_ifIdExists() {
        Usv expected = new Usv("1", "Test", "192.168.1.1", "rrr");
        when(repo.existsById("1")).thenReturn(true);
        when(repo.save(expected)).thenReturn(expected);
        Usv actual = service.updateUsv("1", expected);
        assertEquals(expected, actual);
        verify(repo).existsById("1");
        verify(repo).save(expected);
    }

    @Test
    void updateUsv_shouldThrowNoSuchElementException_whenIdDoesNotExist(){
        when(repo.existsById("1")).thenReturn(false);
        Usv submitted = new Usv("1", "Test", "192.168.1.1", "");
        assertThrows(NoSuchElementException.class, () -> service.updateUsv("1", submitted));
        verify(repo).existsById("1");
    }

    @Test
    void deleteUsv_shouldDeleteUsvUsv_ifIdExists() {
        when(repo.existsById("1")).thenReturn(true);
        service.deleteUsv("1");
        verify(repo).deleteById("1");
    }

    @Test
    void deleteUsv_shouldThrowNoSuchElementException_whenIdDoesNotExist(){
        when(repo.existsById("1")).thenReturn(false);
        assertThrows(NoSuchElementException.class, () -> service.deleteUsv("1"));
    }
}