package de.sieberss.backend.utils;

import de.sieberss.backend.exception.EncryptionException;
import de.sieberss.backend.model.Credentials;
import de.sieberss.backend.model.CredentialsWithoutEncryption;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;

@Service
@RequiredArgsConstructor
public class EncryptionService {
    private final String ALGORITHM = "AES";

    /** Class copied from external source, only my added code at the bottom to be tested
     *
     */
    public String encrypt(String data, String key) throws Exception {
        SecretKeySpec secretKey = new SecretKeySpec(key.getBytes(), ALGORITHM);
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.ENCRYPT_MODE, secretKey);
        byte[] encryptedBytes = cipher.doFinal(data.getBytes());
        return Base64.getEncoder().encodeToString(encryptedBytes);
    }

    public String decrypt(String encryptedData, String key) throws Exception {
        SecretKeySpec secretKey = new SecretKeySpec(key.getBytes(), ALGORITHM);
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.DECRYPT_MODE, secretKey);
        byte[] decryptedBytes = cipher.doFinal(Base64.getDecoder().decode(encryptedData));
        return new String(decryptedBytes);
    }

    public String generateKey() throws Exception {
        KeyGenerator keyGen = KeyGenerator.getInstance(ALGORITHM);
        keyGen.init(128); // Schlüsselgröße
        SecretKey secretKey = keyGen.generateKey();
        return Base64.getEncoder().encodeToString(secretKey.getEncoded());
    }

    /** start of my added code
     *
     */
    private final String key = System.getenv("ENCRYPT_KEY");

    public String encryptPassword(String value) {
        try {
            return encrypt(value, key);
        } catch (Exception e) {
            throw new EncryptionException(e.getMessage());
        }
    }

    public Credentials encryptCredentials(CredentialsWithoutEncryption unencrypted) {
        return new Credentials(unencrypted.id(), unencrypted.user(), encryptPassword(unencrypted.password()), unencrypted.localOnly());
    }

    public String decryptPassword(String value) {
        try {
            return decrypt(value, key);
        }
        catch (Exception e) {
            throw new EncryptionException(e.getMessage());
        }
    }

    public CredentialsWithoutEncryption decryptCredentials(Credentials encrypted) {
        return new CredentialsWithoutEncryption(encrypted.id(), encrypted.user(), decryptPassword(encrypted.password()), encrypted.localOnly());
    }



}
