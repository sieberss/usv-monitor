package de.sieberss.backend.service;

import de.sieberss.backend.model.PowerState;
import de.sieberss.backend.model.ServerDTO;
import de.sieberss.backend.model.Status;
import de.sieberss.backend.model.Ups;
import de.sieberss.backend.utils.UpsStatusSimulator;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Getter
@Setter
@RequiredArgsConstructor
public class StatusService {
    private final UpsService upsService;
    private final ServerService serverService;
    private final UpsStatusSimulator simulator;

    private boolean monitoring = false;
    private Map<String, Status> statusMap = new HashMap<>();
    private Instant startTime;
    private List<Ups> upsList = new ArrayList<>();
    private List<ServerDTO> serverList = new ArrayList<>();

    public List<Status> getAllStatuses() {
        for (Ups ups : upsList) {
            statusMap.put(ups.id(), simulator.getUpsStatus(ups));
        }
        for (ServerDTO server : serverList) {
            setServerStatus(server);
        }
        return new ArrayList<>(statusMap.values());
    }

    public void startMonitoring() {
        if (isMonitoring()) return;
        setServerList(serverService.getServerDTOList());
        setUpsList(upsService.getUpsList());
        setStartTime(Instant.now());
        simulator.simulatePowerOff(startTime, upsList);
    }

    public void stopMonitoring() {
        serverList.clear();
        upsList.clear();
        statusMap.clear();
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
            statusMap.put(server.id(), upsStatus.withState(PowerState.POWER_OFF_LIMIT));
            // to be replaced by call of another service
            shutdownServer(server);
        }
        else statusMap.put(server.id(), upsStatus);
    }

    private void shutdownServer(ServerDTO server) {
        /* in simulation: shutdown executed within 2 seconds*/
        statusMap.put(server.id(), new Status(server.id(), PowerState.SHUTDOWN, Instant.now().plusSeconds(2), 0));
    }

}
