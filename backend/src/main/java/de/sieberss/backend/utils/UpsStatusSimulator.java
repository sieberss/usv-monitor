package de.sieberss.backend.utils;

import de.sieberss.backend.model.PowerState;
import de.sieberss.backend.model.Status;
import de.sieberss.backend.model.Ups;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;
import java.util.List;

@Service
@RequiredArgsConstructor
@Getter
@Setter
public class UpsStatusSimulator {

    // simulated times of PowerOff event
    private String powerOff1Id;
    private String powerOff2Id;
    private Instant begin1, end1, begin2, end2, startTime;

    public Status getUpsStatus(final Ups ups) {
        Instant now = Instant.now();
        if (ups.id().equals(powerOff1Id)) {
            if (now.isBefore(begin1)) {
                return new Status(ups.id(), PowerState.POWER_ON, startTime, 600);
            }
            if (now.isAfter(end1)) {
                long remaining = 600 - Duration.between(begin1, end1).toSeconds() + Duration.between(end1, now).toSeconds();
                return new Status(ups.id(), PowerState.POWER_ON, end1, remaining > 600 ? 600 : remaining);
            }
            return new Status(ups.id(), PowerState.POWER_OFF, begin1, 600 - Duration.between(begin1, now).toSeconds());
        }
        if (ups.id().equals(powerOff2Id)) {
            if (now.isBefore(begin2)) {
                return new Status(ups.id(), PowerState.POWER_ON, startTime, 600);
            }
            if (now.isAfter(end2)) {
                long remaining = 600 - Duration.between(begin2, end2).toSeconds() + Duration.between(end2, now).toSeconds();
                return new Status(ups.id(), PowerState.POWER_ON, end2, remaining > 600 ? 600 : remaining);}
            return new Status(ups.id(), PowerState.POWER_OFF, begin2, 600 - Duration.between(begin2, now).toSeconds());
        }
        return new Status(ups.id(), PowerState.POWER_ON, startTime, 600);
    }

    public void simulatePowerOff(Instant startTime, List<Ups> upsList) {
        this.startTime = startTime;
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


}
