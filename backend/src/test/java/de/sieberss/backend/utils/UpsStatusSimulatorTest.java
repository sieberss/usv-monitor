package de.sieberss.backend.utils;

import de.sieberss.backend.model.Ups;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class UpsStatusSimulatorTest {

    @Test
    void getUpsStatus_shouldReturnUpsStatus() {
        final UpsStatusSimulator upsStatusSimulator = new UpsStatusSimulator();
        Ups ups1 = new Ups("1", "UPS 1", "192.168.1.1", "c");
        Ups ups2 = new Ups("2", "UPS 2", "192.168.1.2", "c");
        Ups ups3 = new Ups("3", "UPS 3", "192.168.1.3", "c");
        upsStatusSimulator.simulatePowerOff(Instant.now(), List.of(ups1, ups2, ups3));
        assertEquals("1", upsStatusSimulator.getUpsStatus(ups1).upsOrServerId());
        assertEquals("2", upsStatusSimulator.getUpsStatus(ups2).upsOrServerId());
        assertEquals("3", upsStatusSimulator.getUpsStatus(ups3).upsOrServerId());
        assertNotNull(upsStatusSimulator.getUpsStatus(ups1).state());
        assertNotNull(upsStatusSimulator.getUpsStatus(ups2).state());
        assertNotNull(upsStatusSimulator.getUpsStatus(ups3).state());
    }
}