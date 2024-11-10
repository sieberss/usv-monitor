package de.sieberss.backend.service;

import de.sieberss.backend.model.PowerState;
import de.sieberss.backend.model.ServerDTO;
import de.sieberss.backend.model.Status;
import de.sieberss.backend.model.Ups;
import de.sieberss.backend.utils.UpsStatusSimulator;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class StatusServiceTest {

    private final UpsService upsService = mock(UpsService.class);
    private final ServerService serverService = mock(ServerService.class);
    private final UpsStatusSimulator statusSimulator = mock(UpsStatusSimulator.class);
    private final StatusService statusService = new StatusService(upsService, serverService, statusSimulator);


    @Test
    void getAllStatuses_shouldReturnMapOfStatuses_OfAllUpsesAndServers_whenMonitoring() {
        Ups ups1 = new Ups("1", "UPS", "localhost", "c");
        Ups ups2 = new Ups("2", "USV", "192.168.1.1", "c");
        when(upsService.getUpsList()).thenReturn(List.of(ups1, ups2));
        when(statusSimulator.getUpsStatus(ups1)).thenReturn(new Status (ups1.id(), PowerState.POWER_ON, null, 0 ));
        when(statusSimulator.getUpsStatus(ups2)).thenReturn(new Status(ups2.id(), PowerState.POWER_OFF, null, 0 ));
        ServerDTO server1 = new ServerDTO("11", "SERVER 1", "192.168.1.11", null, "1", 180);
        ServerDTO server2 = new ServerDTO("12", "SERVER 2", "192.168.1.12", null, "2", 180);
        ServerDTO server3 = new ServerDTO("13", "SERVER 3", "192.168.1.13", null, "2", 180);
        when(serverService.getServerDTOList()).thenReturn(List.of(server1, server2, server3));
        // start monitoring and execute method
        statusService.startMonitoring();
        Map<String,Status> statuses = statusService.getAllStatuses();
        assertEquals(5, statuses.size());
        for (Status element: statuses.values()) assertNotNull(element);
    }

    @Test
    void getAllStatuses_shouldReturnEmptyList_whenNotMonitoring() {
        Ups ups1 = new Ups("1", "UPS", "localhost", "c");
        Ups ups2 = new Ups("2", "USV", "192.168.1.1", "c");
        when(upsService.getUpsList()).thenReturn(List.of(ups1, ups2));
        ServerDTO server1 = new ServerDTO("11", "SERVER 1", "192.168.1.11", null, "1", 180);
        ServerDTO server2 = new ServerDTO("12", "SERVER 2", "192.168.1.12", null, "2", 180);
        ServerDTO server3 = new ServerDTO("13", "SERVER 3", "192.168.1.13", null, "2", 180);
        when(serverService.getServerDTOList()).thenReturn(List.of(server1, server2, server3));
        // execute method
        Map<String,Status> statuses = statusService.getAllStatuses();
        assertEquals(0, statuses.size());
    }
}