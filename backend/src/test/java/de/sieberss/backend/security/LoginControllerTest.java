package de.sieberss.backend.security;

import de.sieberss.backend.model.Credentials;
import de.sieberss.backend.model.CredentialsWithoutEncryption;
import de.sieberss.backend.repo.CredentialsRepo;
import de.sieberss.backend.utils.EncryptionService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Base64;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@AutoConfigureMockMvc
class LoginControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private EncryptionService encryptionService;
    @Autowired
    private CredentialsRepo credentialsRepo;

    @WithMockUser
    @Test
    void getMe_shouldReturnMeUsername_whenLoggedIn() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/login"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string("user"));
    }

    @Test
    void getMe_shouldReturnAnonymousUser_whenNotLoggedIn() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/login"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string("anonymousUser"));
    }

    @Test
    void login_shouldReturnUserName_whenCredentialsAreCorrect() throws Exception {
        CredentialsWithoutEncryption decrypted = new CredentialsWithoutEncryption("1", "testuser", "testpassword", false);
        Credentials encrypted = encryptionService.encryptCredentials(decrypted);
        credentialsRepo.save(encrypted);
        String auth = "testuser:testpassword";
        String encoded =  Base64.getEncoder().encodeToString(auth.getBytes());
        mockMvc.perform(
                        MockMvcRequestBuilders.post("/api/login")
                                .header(HttpHeaders.AUTHORIZATION, "Basic " + encoded)
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string("testuser"));
    }

    @Test
    void login_shouldReturnStatus401_whenPasswordIsWrong() throws Exception {
        CredentialsWithoutEncryption decrypted = new CredentialsWithoutEncryption("1", "testuser", "testpassword", false);
        Credentials encrypted = encryptionService.encryptCredentials(decrypted);
        credentialsRepo.save(encrypted);
        String auth = "testuser:testpass";
        String basicAuthHeader =  Base64.getEncoder().encodeToString(auth.getBytes());
        mockMvc.perform(
                        MockMvcRequestBuilders.post("/api/login")
                                .header(HttpHeaders.AUTHORIZATION, "Basic " + basicAuthHeader)
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(MockMvcResultMatchers.status().isUnauthorized());
    }

    @Test
    void login_shouldReturnStatus401_whenUserNotInDatabase() throws Exception {
        String auth = "testuser:testpass";
        String basicAuthHeader =  Base64.getEncoder().encodeToString(auth.getBytes());
        mockMvc.perform(
                        MockMvcRequestBuilders.post("/api/login")
                                .header(HttpHeaders.AUTHORIZATION, "Basic " + basicAuthHeader)
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(MockMvcResultMatchers.status().isUnauthorized());
    }

    @Test
    void register_shouldAddUser_whenNotExists() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/api/login/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                            "user": "testuser",
                            "password": "testpassword",
                            "global" : false
                        }
                        """))
                .andExpect(MockMvcResultMatchers.status().isOk());
        UserDetails user = User.withUsername("testuser")
                .password("testpassword")
                .roles("USER")
                .build();
        UsernamePasswordAuthenticationToken auth =
                new UsernamePasswordAuthenticationToken(user, user.getPassword(), user.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(auth);
        mockMvc.perform(MockMvcRequestBuilders.get("/api/credentials"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json("""
                        [{
                            "user": "testuser",
                            "password": "testpassword",
                            "global" : false
                        }]
                        """))
                        .andExpect(MockMvcResultMatchers.jsonPath("$[0].id").isNotEmpty());
        SecurityContextHolder.clearContext();
    }

    @Test
    void register_shouldTriggerErrorMessage_whenUserAlreadyExists() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/api/login/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                        {
                            "user": "testuser",
                            "password": "testpassword",
                            "global" : false
                        }
                        """))
                .andExpect(MockMvcResultMatchers.status().isOk());
        mockMvc.perform(MockMvcRequestBuilders.post("/api/login/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                          {
                            "user": "testuser",
                            "password": "testpass",
                            "global" : false
                          }
                        """))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.content().json("""
                        {
                        "message": "Illegal argument",
                        "id":  "testuser already exists"
                        }
                """));
    }

    @WithMockUser
    @Test
    void logout_shouldClearContext() throws Exception {
        // Schritt 2: Geschützten Endpunkt aufrufen (vor dem Logout) und sicherstellen, dass der Zugriff erfolgreich ist
        mockMvc.perform(MockMvcRequestBuilders.post("/api/credentials")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                 {
                                     "user": "anotheruser",
                                     "password": "password",
                                     "global": true
                                 }
                          """))
                .andExpect(MockMvcResultMatchers.status().isOk());  // Erfolgreicher Zugriff bestätigt Authentifizierung
        // Schritt 3: Logout-Anfrage senden
        mockMvc.perform(MockMvcRequestBuilders.get("/logout")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNoContent());  //
        // Schritt 4: Erneuten Zugriff auf den geschützten Endpunkt versuchen, was fehlschlagen sollte
        mockMvc.perform(MockMvcRequestBuilders.post("/api/credentials")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                 {
                                     "user": "anotheruser",
                                     "password": "password",
                                     "global": true
                                 }
                          """))
                .andExpect(MockMvcResultMatchers.status().isUnauthorized());  // Zugriff verweigert nach logout
    }
}