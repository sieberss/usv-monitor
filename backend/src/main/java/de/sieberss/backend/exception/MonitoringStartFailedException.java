package de.sieberss.backend.exception;

public class MonitoringStartFailedException extends RuntimeException {
    public MonitoringStartFailedException(String message) {
        super(message);
    }
}
