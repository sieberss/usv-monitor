package de.sieberss.backend.security;

import de.sieberss.backend.model.Credentials;
import de.sieberss.backend.model.CredentialsDTO;
import de.sieberss.backend.repo.CredentialsRepo;
import de.sieberss.backend.service.CredentialsService;
import de.sieberss.backend.utils.IdService;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;


@RequiredArgsConstructor
class LoginServiceTest {
    private final CredentialsRepo repo = mock(CredentialsRepo.class);
    private final IdService idService = mock(IdService.class);
    private final CredentialsService credentialsService = new CredentialsService(repo, idService);
    private final LoginService loginService = new LoginService(repo, credentialsService);

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
        String encryptedPassword = EncryptionService.encryptPassword(password);
        CredentialsDTO submitted = new CredentialsDTO("", username, password, false);
        Credentials encrypted = new Credentials("1", username, encryptedPassword, false);
        when(idService.generateId()).thenReturn("1");
        when(repo.save(encrypted)).thenReturn(encrypted);
        // execute method
        loginService.register(submitted);
        verify(idService, times(1)).generateId();
        verify(repo, times(1)).save(encrypted);
    }

    @Test
    void register_shouldThrowIllegalArgumentException_whenAlreadyExists() {
        CredentialsDTO submitted = new CredentialsDTO("", "testuser", "password", false);
        when(repo.findByUser("testuser")).thenReturn(Optional.of(
                new Credentials("1", "testuser", "gjklrjeir34", false)));
        // execute method
        assertThrows(IllegalArgumentException.class, () -> loginService.register(submitted));
        verify(repo, times(1)).findByUser("testuser");
    }
}