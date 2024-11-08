package de.sieberss.backend.utils;

import de.sieberss.backend.model.Credentials;
import de.sieberss.backend.model.CredentialsWithoutEncryption;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EncryptionServiceTest {
    
    @Test
    void encryptPassword_shouldEncryptPassword_decryptPassword_shouldRestoreIt() throws Exception {
        EncryptionService.setTestKey();
        final String password = "password";
        final String encryptedPassword = EncryptionService.encryptPassword(password);
        final String decryptedPassword = EncryptionService.decryptPassword(encryptedPassword);
        assertEquals(password, decryptedPassword);
        assertNotEquals(password, encryptedPassword);
        assertNotEquals("", encryptedPassword);
    }

    @Test
    void encryptCredentials_shouldEncryptCredentials_decryptCredentials_shouldRestoreIt() throws Exception {
        EncryptionService.setTestKey();
        CredentialsWithoutEncryption credentialsWithoutEncryption = new CredentialsWithoutEncryption("1", "user", "password", true);
        Credentials encryptedCredentials = EncryptionService.encryptCredentials(credentialsWithoutEncryption);
        CredentialsWithoutEncryption decryptedCredentials = EncryptionService.decryptCredentials(encryptedCredentials);

        assertEquals(credentialsWithoutEncryption, decryptedCredentials);

        // fields after encryption must be equal, except password (different and not empty)
        assertEquals(credentialsWithoutEncryption.id(), encryptedCredentials.id());
        assertEquals(credentialsWithoutEncryption.user(), encryptedCredentials.user());
        assertEquals(credentialsWithoutEncryption.global(), encryptedCredentials.global());
        assertNotEquals(credentialsWithoutEncryption.password(), encryptedCredentials.password());
        assertNotEquals("", encryptedCredentials.password());
    }

    @Test
    void encryptCredentials_shouldEncryptandDecryptNullToNull() throws Exception {
        EncryptionService.setTestKey();
        assertNull(EncryptionService.encryptCredentials(null));
        assertNull(EncryptionService.decryptCredentials(null));
    }

}