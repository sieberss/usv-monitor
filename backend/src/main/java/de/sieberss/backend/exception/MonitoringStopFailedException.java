package de.sieberss.backend.exception;

public class MonitoringStopFailedException extends RuntimeException {
    public MonitoringStopFailedException(String message) {
        super(message);
    }
}
