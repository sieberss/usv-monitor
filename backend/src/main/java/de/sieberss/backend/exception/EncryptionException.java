package de.sieberss.backend.exception;

public class EncryptionException extends RuntimeException {
  public EncryptionException(String message) {
    super(message);
  }
}
