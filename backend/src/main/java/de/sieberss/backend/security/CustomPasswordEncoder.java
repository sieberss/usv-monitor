package de.sieberss.backend.security;

import de.sieberss.backend.utils.EncryptionService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomPasswordEncoder implements PasswordEncoder {
    private final EncryptionService encryptionService;

    public String encode(CharSequence rawPassword) {
        return encryptionService.encryptPassword(rawPassword.toString());
    }

    public boolean matches(CharSequence rawPassword, String encodedPassword) {
        return !rawPassword.isEmpty() && encode(rawPassword).equals(encodedPassword);
    }
}
