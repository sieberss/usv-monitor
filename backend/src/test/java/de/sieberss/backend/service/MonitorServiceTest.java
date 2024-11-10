package de.sieberss.backend.service;

import de.sieberss.backend.model.PowerState;
import de.sieberss.backend.model.Status;
import de.sieberss.backend.model.StatusResponse;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class MonitorServiceTest {

    private final StatusService statusService = mock(StatusService.class);
    private final MonitorService monitorService = new MonitorService(statusService);

    @Test
    void getAllStatuses_shouldReturnResponse_withTrueAndNonemptyList_whenMonitoring() {
        Instant instant1 = Instant.parse("2020-01-01T00:00:00Z");
        Instant instant2 = Instant.parse("2020-01-03T00:00:00Z");
        Status status1 = new Status("1", PowerState.POWER_ON, instant1, 200);
        Status status2 = new Status("2", PowerState.POWER_OFF, instant2, 200);
        Status status3 = new Status("3", PowerState.POWER_OFF, instant2, 200);
        when(statusService.isMonitoring()).thenReturn(true);
        when(statusService.getAllStatuses()).thenReturn(List.of(status1, status2, status3));
        StatusResponse expected = new StatusResponse(true, List.of(status1, status2, status3));
        //  execute method
        StatusResponse actual = monitorService.getAllStatuses();
        assertEquals(expected, actual);
        verify(statusService).isMonitoring();
        verify(statusService).getAllStatuses();
    }

    @Test
    void getAllStatuses_shouldReturnResponse_withFalseAndEmptyList_whenNotMonitoring() {
        when(statusService.isMonitoring()).thenReturn(false);
        StatusResponse expected = new StatusResponse(false, List.of());
        StatusResponse actual = monitorService.getAllStatuses();
        assertEquals(expected, actual);
        verify(statusService).isMonitoring();
    }

    @Test
    void changeMode_shouldDoNothing_WhenNotMonitoringAndNotChanged() {
        when(statusService.isMonitoring()).thenReturn(false);
        boolean actual = monitorService.changeMode(false);
        assertFalse(actual);
        verify(statusService).isMonitoring();
        verifyNoMoreInteractions(statusService);
    }
    @Test
    void changeMode_shouldDoNothing_WhenMonitoringAndNotChanged() {
        when(statusService.isMonitoring()).thenReturn(true);
        boolean actual = monitorService.changeMode(true);
        assertTrue(actual);
        verify(statusService).isMonitoring();
        verifyNoMoreInteractions(statusService);
    }

    @Test
    void changeMode_shouldStartMonitoring_WhenNotMonitoringAndChanged() {
        when(statusService.isMonitoring()).thenReturn(false);
        boolean actual = monitorService.changeMode(true);
        assertTrue(actual);
        verify(statusService).isMonitoring();
        verify(statusService).startMonitoring();
    }

    @Test
    void changeMode_shouldStopMonitoring_WhenMonitoringAndChanged() {
        when(statusService.isMonitoring()).thenReturn(true);
        boolean actual = monitorService.changeMode(false);
        assertFalse(actual);
        verify(statusService).isMonitoring();
        verify(statusService).stopMonitoring();
    }
}