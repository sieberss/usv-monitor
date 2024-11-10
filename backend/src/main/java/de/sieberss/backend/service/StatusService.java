package de.sieberss.backend.service;

import de.sieberss.backend.model.PowerState;
import de.sieberss.backend.model.ServerDTO;
import de.sieberss.backend.model.Status;
import de.sieberss.backend.model.Ups;
import de.sieberss.backend.utils.UpsStatusSimulator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class StatusService {
    private final UpsService upsService;
    private final ServerService serverService;
    private final UpsStatusSimulator simulator;

    private boolean monitoring = false;
    private final Map<String, Status> statusMap = new HashMap<>();
    private List<Ups> upsList = new ArrayList<>();
    private List<ServerDTO> serverList = new ArrayList<>();

    public Map<String,Status> getAllStatuses() {
        for (Ups ups : upsList) {
            statusMap.put(ups.id(), simulator.getUpsStatus(ups));
        }
        for (ServerDTO server : serverList) {
            setServerStatus(server);
        }
        return new HashMap<>(statusMap);
    }

    public void startMonitoring() {
        if (monitoring) return;
        serverList = serverService.getServerDTOList();
        upsList = upsService.getUpsList();
        simulator.simulatePowerOff(Instant.now(), upsList);
        monitoring = true;
    }

    public void stopMonitoring() {
        serverList = new ArrayList<>();
        upsList = new ArrayList<>();
        statusMap.clear();
        monitoring = false;
    }

        /** change state of UPS from POWER_OFF to POWER_OFF_LIMIT when shutdownTime for server is reached */
    private void setServerStatus(ServerDTO server){
        Status serverStatus = statusMap.get(server.id());
        /* when server is shut down or not connected to a UPS, this status persists */
        if (serverStatus != null && (serverStatus.state() == PowerState.SHUTDOWN || serverStatus.state() == PowerState.UNKNOWN)) {
            return;
        }
        /* not connected to a UPS => Status unknown */
        if (server.upsId() == null || server.upsId().isEmpty()) {
            statusMap.put(server.id(), new Status(server.id(), PowerState.UNKNOWN, Instant.now(), 0));
        }
        Status upsStatus = statusMap.get(server.upsId());
        /* change status to POWER_OFF_LIMIT when limit is reached, trigger shutdown*/
        if ((upsStatus.state() == PowerState.POWER_OFF) && (upsStatus.remaining() <= server.shutdownTime())) {
            statusMap.put(server.id(), new Status(server.id(), PowerState.POWER_OFF_LIMIT, Instant.now(), upsStatus.remaining()));
            // to be replaced by call of another service
            shutdownServer(server);
        }
        else statusMap.put(server.id(), new Status(server.id(), upsStatus.state(), upsStatus.timestamp(), upsStatus.remaining()));
    }

    private void shutdownServer(ServerDTO server) {
        /* in simulation: shutdown executed within 2 seconds*/
        statusMap.put(server.id(), new Status(server.id(), PowerState.SHUTDOWN, Instant.now().plusSeconds(2), 0));
    }

}
