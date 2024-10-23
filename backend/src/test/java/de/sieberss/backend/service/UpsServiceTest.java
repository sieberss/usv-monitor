package de.sieberss.backend.service;

import de.sieberss.backend.model.Ups;
import de.sieberss.backend.repo.UpsRepo;
import de.sieberss.backend.utils.IdService;
import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.*;

import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class UpsServiceTest {

    private final UpsRepo repo = mock(UpsRepo.class);
    private final IdService idService = mock(IdService.class);
    private final UpsService service = new UpsService(repo, idService);


    @Test
    void getUpsList_shouldReturnEmptyList_whenRepoIsEmpty() {
        List<Ups> expected = Collections.emptyList();
        when(repo.findAll()).thenReturn(expected);
        List<Ups> actual = service.getUpsList();
        assertEquals(expected, actual);
        verify(repo).findAll();
    }

    @Test
    void getUpsList_shouldReturnContent_whenRepoIsFilled() {
        List<Ups> expected = List.of(new Ups("1", "Test-UPS", "192.168.1.1", ""));
        when(repo.findAll()).thenReturn(expected);
        List<Ups> actual = service.getUpsList();
        assertEquals(expected, actual);
        verify(repo).findAll();
    }

    @Test
    void getUpsById_shouldReturnObject_whenIdExists(){
        Ups expected = new Ups("1", "Test-UPS", "192.168.1.1", "");
        when(repo.findById("1")).thenReturn(Optional.of(expected));
        Ups actual = service.getUpsById("1");
        assertEquals(expected, actual);
        verify(repo).findById("1");
    }

    @Test
    void getUpsById_shouldThrowNoSuchElementException_whenIdDoesNotExist(){
        when(repo.findById("1")).thenReturn(Optional.empty());
        assertThrows(NoSuchElementException.class, () -> service.getUpsById("1"));
        verify(repo).findById("1");
    }

    @Test
    void createUps_shouldCreateUpsWithSubmittedData() {
        when(idService.generateId()).thenReturn("abc");
        Ups submitted = new Ups("1", "Test", "192.168.1.1", "");
        Ups expected = new Ups("abc", "Test", "192.168.1.1", "");
        when(repo.save(expected)).thenReturn(expected);
        Ups actual = service.createUps(submitted);
        verify(repo).save(expected);
        assertEquals(expected, actual);
    }

    @Test
    void updateUps_shouldUpdateUpsWithSubmittedData_ifIdExists() {
        Ups submitted = new Ups(null, "Test", "192.168.1.1", "rrr");
        Ups expected = new Ups("1", "Test", "192.168.1.1", "rrr");
        when(repo.existsById("1")).thenReturn(true);
        when(repo.save(expected)).thenReturn(expected);
        Ups actual = service.updateUps("1", submitted);
        assertEquals(expected, actual);
        verify(repo).existsById("1");
        verify(repo).save(expected);
    }

    @Test
    void updateUps_shouldThrowNoSuchElementException_whenIdDoesNotExist(){
        when(repo.existsById("1")).thenReturn(false);
        Ups submitted = new Ups("1", "Test", "192.168.1.1", "");
        assertThrows(NoSuchElementException.class, () -> service.updateUps("1", submitted));
        verify(repo).existsById("1");
    }

    @Test
    void deleteUps_shouldDeleteUpsUps_ifIdExists() {
        when(repo.existsById("1")).thenReturn(true);
        service.deleteUps("1");
        verify(repo).existsById("1");
        verify(repo).deleteById("1");
    }

    @Test
    void deleteUps_shouldThrowNoSuchElementException_whenIdDoesNotExist(){
        when(repo.existsById("1")).thenReturn(false);
        assertThrows(NoSuchElementException.class, () -> service.deleteUps("1"));
        verify(repo).existsById("1");
    }
}