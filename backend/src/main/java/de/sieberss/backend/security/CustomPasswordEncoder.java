package de.sieberss.backend.security;

import de.sieberss.backend.utils.EncryptionService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class CustomPasswordEncoder implements PasswordEncoder {

    public String encode(CharSequence rawPassword) {
        return EncryptionService.encryptPassword(rawPassword.toString());
    }

    public boolean matches(CharSequence rawPassword, String encodedPassword) {
        return !rawPassword.isEmpty() && encode(rawPassword).equals(encodedPassword);
    }
}
