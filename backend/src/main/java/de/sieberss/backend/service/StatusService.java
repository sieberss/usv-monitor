package de.sieberss.backend.service;

import de.sieberss.backend.model.PowerState;
import de.sieberss.backend.model.ServerDTO;
import de.sieberss.backend.model.Status;
import de.sieberss.backend.model.Ups;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Service;

import java.time.Duration;
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

    private boolean monitoring = false;
    private Map<String, Status> statusMap = new HashMap<>();
    private Instant startTime;
    private List<Ups> upsList = new ArrayList<>();
    private List<ServerDTO> serverList = new ArrayList<>();

    // simulated times of PowerOff event
    private String powerOff1Id;
    private String powerOff2Id;
    private Instant begin1, end1, begin2, end2;

    public List<Status> getAllStatuses() {
        for (Ups ups : upsList) {
            statusMap.put(ups.id(), getUpsStatus(ups));
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
        simulatePowerOff();
    }

    public void stopMonitoring() {
        serverList.clear();
        upsList.clear();
        statusMap.clear();
    }

    private Status getUpsStatus(final Ups ups) {
        // in real application to be replaced by call of another service
        return getSimulatedUpsStatus(ups);
    }

    private Status getSimulatedUpsStatus(final Ups ups) {
        Instant now = Instant.now();
        if (ups.id().equals(powerOff1Id)) {
            if (now.isBefore(begin1)) {
                return new Status(ups.id(), PowerState.POWER_ON, startTime, 600);
            }
            if (now.isAfter(end1)) {
                long remaining = 600 - Duration.between(begin1, end1).toSeconds() + Duration.between(end1, now).toSeconds();
                return new Status(ups.id(), PowerState.POWER_ON, end1, remaining > 600 ? 600 : remaining);
            }
            return new Status(ups.id(), PowerState.POWER_OFF, begin1, 600 - Duration.between(now, begin1).toSeconds());
        }
        if (ups.id().equals(powerOff2Id)) {
            if (now.isBefore(begin2)) {
                return new Status(ups.id(), PowerState.POWER_ON, startTime, 600);
            }
            if (now.isAfter(end2)) {
                long remaining = 600 - Duration.between(begin2, end2).toSeconds() + Duration.between(end2, now).toSeconds();
                return new Status(ups.id(), PowerState.POWER_ON, end2, remaining > 600 ? 600 : remaining);}
            return new Status(ups.id(), PowerState.POWER_OFF, begin2, 600 - Duration.between(now, begin2).toSeconds());
        }
        return new Status(ups.id(), PowerState.POWER_ON, startTime, 600);
    }

    private void simulatePowerOff() {
        int first = (int) (Math.random() * upsList.size());
        int second = (int) (Math.random() * upsList.size());
        if (first == second) {second = (second + 1) % upsList.size();}
        setPowerOff1Id(upsList.get(first).id());
        setPowerOff2Id(upsList.get(second).id());
        setBegin1(startTime.plus(Duration.ofSeconds(10)));
        setEnd1(begin1.plus(Duration.ofSeconds(120)));
        setBegin2(startTime.plus(Duration.ofSeconds(20)));
        setEnd2(begin2.plus(Duration.ofSeconds(30)));
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
