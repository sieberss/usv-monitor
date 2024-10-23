package de.sieberss.backend.utils;

import de.sieberss.backend.model.Credentials;
import de.sieberss.backend.model.CredentialsWithoutEncryption;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EncryptionServiceTest {

    private final EncryptionService encryptionService = new EncryptionService();

    @Test
    void encryptPassword_shouldEncryptPassword_decryptPassword_shouldRestoreIt() {
        final String password = "password";
        final String encryptedPassword = encryptionService.encryptPassword(password);
        final String decryptedPassword = encryptionService.decryptPassword(encryptedPassword);
        assertEquals(password, decryptedPassword);
        assertNotEquals(password, encryptedPassword);
        assertNotEquals("", encryptedPassword);
    }

    @Test
    void encryptCredentials_shouldEncryptCredentials_decryptCredentials_shouldRestoreIt() {
        CredentialsWithoutEncryption credentialsWithoutEncryption = new CredentialsWithoutEncryption("1", "user", "password", true);
        Credentials encryptedCredentials = encryptionService.encryptCredentials(credentialsWithoutEncryption);
        CredentialsWithoutEncryption decryptedCredentials = encryptionService.decryptCredentials(encryptedCredentials);

        assertEquals(credentialsWithoutEncryption, decryptedCredentials);

        // fields after encryption must be equal, except password (different and not empty)
        assertEquals(credentialsWithoutEncryption.id(), encryptedCredentials.id());
        assertEquals(credentialsWithoutEncryption.user(), encryptedCredentials.user());
        assertEquals(credentialsWithoutEncryption.localOnly(), encryptedCredentials.localOnly());
        assertNotEquals(credentialsWithoutEncryption.password(), encryptedCredentials.password());
        assertNotEquals("", encryptedCredentials.password());
    }

}