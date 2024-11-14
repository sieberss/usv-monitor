package de.sieberss.backend.utils;

import de.sieberss.backend.exception.EncryptionException;
import de.sieberss.backend.model.Credentials;
import de.sieberss.backend.model.CredentialsDTO;
import org.springframework.stereotype.Service;

import javax.crypto.*;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

@Service
public class EncryptionService {

    private static final String ALGORITHM = "AES";

    /** Class copied from external source, only my added code at the bottom to be tested
     *
     */
    public static String encrypt(String data, String key) throws NoSuchAlgorithmException, NoSuchPaddingException, IllegalStateException, IllegalBlockSizeException, BadPaddingException, InvalidKeyException {
        SecretKeySpec secretKey = new SecretKeySpec(key.getBytes(), ALGORITHM);
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.ENCRYPT_MODE, secretKey);
        byte[] encryptedBytes = cipher.doFinal(data.getBytes());
        return Base64.getEncoder().encodeToString(encryptedBytes);
    }

    public static String decrypt(String encryptedData, String key) throws NoSuchAlgorithmException, NoSuchPaddingException, IllegalStateException, IllegalBlockSizeException, BadPaddingException, InvalidKeyException {
        SecretKeySpec secretKey = new SecretKeySpec(key.getBytes(), ALGORITHM);
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.DECRYPT_MODE, secretKey);
        byte[] decryptedBytes = cipher.doFinal(Base64.getDecoder().decode(encryptedData));
        return new String(decryptedBytes);
    }

    public static String generateKey() throws NoSuchAlgorithmException, IllegalStateException{
        KeyGenerator keyGen = KeyGenerator.getInstance(ALGORITHM);
        keyGen.init(128); // Schlüsselgröße
        SecretKey secretKey = keyGen.generateKey();
        return Base64.getEncoder().encodeToString(secretKey.getEncoded());
    }

    /** start of my added code
     *
     */
    private static String key;

    public static void setApplicationKey(String key){
        EncryptionService.key = key;
    }

    public static void setTestKey() throws NoSuchAlgorithmException, IllegalStateException {
        key = generateKey();
    }

    public static String encryptPassword(String value) {
        try {
            return encrypt(value, key);
        } catch (Exception e) {
            throw new EncryptionException(e.getMessage());
        }
    }

    public static Credentials encryptCredentials(CredentialsDTO unencrypted) {
        return unencrypted == null
                ? null
                : new Credentials(unencrypted.id(), unencrypted.user(), encryptPassword(unencrypted.password()), unencrypted.global());
    }

    public static String decryptPassword(String value) {
        try {
            return decrypt(value, key);
        }
        catch (Exception e) {
            throw new EncryptionException(e.getMessage());
        }
    }

    public static CredentialsDTO decryptCredentials(Credentials encrypted) {
        return encrypted == null
                ? null
                : new CredentialsDTO(encrypted.id(), encrypted.user(), decryptPassword(encrypted.password()), encrypted.global());
    }



}
