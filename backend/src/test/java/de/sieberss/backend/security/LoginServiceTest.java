package de.sieberss.backend.security;

import de.sieberss.backend.model.Credentials;
import de.sieberss.backend.model.CredentialsWithoutEncryption;
import de.sieberss.backend.repo.CredentialsRepo;
import de.sieberss.backend.utils.EncryptionService;
import de.sieberss.backend.utils.IdService;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.argon2.Argon2PasswordEncoder;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;


@RequiredArgsConstructor
class LoginServiceTest {
    private final CredentialsRepo repo = mock(CredentialsRepo.class);
    private final IdService idService = mock(IdService.class);
    private final LoginService loginService = new LoginService(repo, idService);

    @BeforeAll
    static void setUp() throws Exception {
        EncryptionService.setTestKey();
    }

    @Test
    void loadUserByUsername_shouldReturnUserObjectWhenNameIsInDatabase() {
        String username = "testuser";
        String encryptedPassword = "zuerer34";
        User user = new User(username, encryptedPassword, List.of());
        when(repo.findByUser(username)).thenReturn(Optional.of(
                new Credentials("1", username, encryptedPassword, false)));
        // execute method
        UserDetails actual = loginService.loadUserByUsername(username);
        verify(repo, times(1)).findByUser(username);
        assertEquals(user, actual);
    }

    @Test
    void loadUserByUsername_shouldThrowUsernameNotFoundException_whenNameIsNotInDatabase() {
        String username = "testuser";
        assertThrows(UsernameNotFoundException.class, () -> loginService.loadUserByUsername(username));

    }
    @Test
    void register_shouldAddNewUserToDatabase_ifNotAlreadyExists() {
        String username = "testuser";
        String password = "password";
        Argon2PasswordEncoder encoder = Argon2PasswordEncoder.defaultsForSpringSecurity_v5_8();
        String encryptedPassword = encoder.encode(password);
        CredentialsWithoutEncryption submitted = new CredentialsWithoutEncryption("", username, password, false);
        Credentials encrypted = new Credentials("1", username, encryptedPassword, false);
        when(repo.findByUser(username)).thenReturn(Optional.empty());
        when(idService.generateId()).thenReturn("1");
        when(repo.save(encrypted)).thenReturn(encrypted);
        // execute method
        loginService.register(submitted);
        verify(repo, times(1)).findByUser(username);
        verify(idService, times(1)).generateId();
        verify(repo, times(1)).save(any());
    }

    @Test
    void register_shouldThrowIllegalArgumentException_whenAlreadyExists() {
        CredentialsWithoutEncryption submitted = new CredentialsWithoutEncryption("", "testuser", "password", false);
        when(repo.findByUser("testuser")).thenReturn(Optional.of(
                new Credentials("1", "testuser", "gjklrjeir34", false)));
        // execute method
        assertThrows(IllegalArgumentException.class, () -> loginService.register(submitted));
        verify(repo, times(1)).findByUser("testuser");
    }
}