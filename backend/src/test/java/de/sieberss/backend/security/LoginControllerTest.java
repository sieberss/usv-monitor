package de.sieberss.backend.security;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
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
    void registerAndThenLoginWithSameData_shouldReturnUserName_whenDatabaseIsEmptyBefore() throws Exception {
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
        String auth = "testuser:testpassword";
        String basicAuthHeader =  Base64.getEncoder().encodeToString(auth.getBytes());
        mockMvc.perform(
                        MockMvcRequestBuilders.post("/api/login")
                                .header(HttpHeaders.AUTHORIZATION, "Basic " + basicAuthHeader)
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string("testuser"));
    }

    @Test
    void login_shouldReturnStatus401_whenPasswordIsWrong() throws Exception {
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
        String auth = "testuser:testpasswordwrong";
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