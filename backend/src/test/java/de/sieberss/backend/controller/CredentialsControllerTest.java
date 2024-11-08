package de.sieberss.backend.controller;

import de.sieberss.backend.model.CredentialsWithoutEncryption;
import de.sieberss.backend.repo.CredentialsRepo;
import de.sieberss.backend.utils.EncryptionService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@SpringBootTest
@WithMockUser
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@AutoConfigureMockMvc
class CredentialsControllerTest {

    @Autowired
    private MockMvc mvc;
    @Autowired
    private CredentialsRepo repo;

    @BeforeAll
    static void setUp() throws Exception {
        EncryptionService.setTestKey();
    }

    @Test
    void getCredentialsList_shouldReturnListWithOneObject_whenOneObjectWasSavedInRepository() throws Exception {
        CredentialsWithoutEncryption decrypted = new CredentialsWithoutEncryption("1", "user", "password", true);
        repo.save(EncryptionService.encryptCredentials(decrypted));
        mvc.perform(MockMvcRequestBuilders.get("/api/credentials"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(
                        """
                                [
                                 {
                                     "id": "1",
                                     "user": "user",
                                     "password": "password",
                                     "global": true
                                 }
                                ]
                                """
                ));
    }

    @Test
    void getCredentialsList_shouldReturnEmptyList_initially() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get("/api/credentials"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(
                        """
                                [
                             
                                ]
                                """
                ));
    }

    @Test
    void getCredentialsById_shouldReturnCredentials_whenIdExists() throws Exception {
        CredentialsWithoutEncryption decrypted = new CredentialsWithoutEncryption("1", "user", "password", true);
        repo.save(EncryptionService.encryptCredentials(decrypted));
        mvc.perform(MockMvcRequestBuilders.get("/api/credentials/1"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json("""
                                {
                                     "id": "1",
                                     "user": "user",
                                     "password": "password",
                                     "global": true
                                 }
                                """
                ));
    }

    @Test
    void getCredentialsById_shouldTriggerErrorMessage_whenIdDoesNotExist() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get("/api/credentials/1"))
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.content().json("""
                                     {
                                     "message": "Credentials not found",
                                     "id": "1"
                                     }
                                     """
                ));

    }

    @Test
    void createCredentials_shouldReturnSubmittedObjectWithNewId() throws Exception {
        mvc.perform(MockMvcRequestBuilders.post("/api/credentials")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                 {
                                     "user": "user",
                                     "password": "password",
                                     "global": true
                                 }
                          """))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").isNotEmpty())
                .andExpect(MockMvcResultMatchers.content().json("""
                                 {
                                     "user": "user",
                                     "password": "password",
                                     "global": true
                                 }
                             """));
    }

    @Test
    void createCredentials_shouldTriggerErrorMessage_whenUserEmpty() throws Exception {
        mvc.perform(MockMvcRequestBuilders.post("/api/credentials")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                 {
                                     "user": "",
                                     "password": "password",
                                     "global": true
                                 }
                          """))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.content().json("""
                                {
                                     "message": "Illegal argument",
                                     "id": "Missing username and/or password"
                                     }
                             """));
    }

    @Test
    void createCredentials_shouldTriggerErrorMessage_whenUserNull() throws Exception {
        mvc.perform(MockMvcRequestBuilders.post("/api/credentials")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                 {
                                     "user": null,
                                     "password": "password",
                                     "global": true
                                 }
                          """))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.content().json("""
                                {
                                     "message": "Illegal argument",
                                     "id": "Missing username and/or password"
                                     }
                             """));
    }
    @Test
    void createCredentials_shouldTriggerErrorMessage_whenPasswordEmpty() throws Exception {
        mvc.perform(MockMvcRequestBuilders.post("/api/credentials")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                 {
                                     "user": "user",
                                     "password": "",
                                     "global": true
                                 }
                          """))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.content().json("""
                                {
                                     "message": "Illegal argument",
                                     "id": "Missing username and/or password"
                                     }
                             """));
    }
    @Test
    void createCredentials_shouldTriggerErrorMessage_whenPasswordNull() throws Exception {
        mvc.perform(MockMvcRequestBuilders.post("/api/credentials")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                 {
                                     "user": "user",
                                     "password": null,
                                     "global": true
                                 }
                          """))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.content().json("""
                                {
                                     "message": "Illegal argument",
                                     "id": "Missing username and/or password"
                                     }
                             """));
    }

    @Test
    void updateCredentials_shouldUpdateCredentials_whenIdExists() throws Exception {
        CredentialsWithoutEncryption decrypted = new CredentialsWithoutEncryption("1", "user", "password", true);
        repo.save(EncryptionService.encryptCredentials(decrypted));
        mvc.perform(MockMvcRequestBuilders.put("/api/credentials/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                 {
                                     "id": "1jkgjdkrte",
                                     "user": "user",
                                     "password": "password",
                                     "global": true
                                 }
                          """))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").isNotEmpty())
                .andExpect(MockMvcResultMatchers.content().json("""
                             {
                                     "id": "1",
                                     "user": "user",
                                     "password": "password",
                                     "global": true
                                 }
                             """));
    }

    @Test
    void updateCredentials_shouldTriggerErrorMessage_whenIdDoesNotExist() throws Exception {
        mvc.perform(MockMvcRequestBuilders.put("/api/credentials/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                                 {
                                     "id": "1",
                                     "user": "user",
                                     "password": "password",
                                     "global": true
                                 }
                          """))
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.content().json("""
                                     {
                                     "message": "Credentials not found",
                                     "id": "1"
                                     }
                                     """
                ));

    }

    @Test
    void deleteCredentials_shouldDeleteUps_whenIdExists() throws Exception {
        CredentialsWithoutEncryption decrypted1 = new CredentialsWithoutEncryption("1", "user", "password", true);
        CredentialsWithoutEncryption decrypted2 = new CredentialsWithoutEncryption("2", "someone", "secret", false);
        repo.save(EncryptionService.encryptCredentials(decrypted1));
        repo.save(EncryptionService.encryptCredentials(decrypted2));
        mvc.perform(MockMvcRequestBuilders.delete("/api/credentials/2"))
            .andExpect(MockMvcResultMatchers.status().isOk());
        mvc.perform(MockMvcRequestBuilders.get("/api/credentials"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json("""
                                  [{
                                     "id": "1",
                                     "user": "user",
                                     "password": "password",
                                     "global": true
                                 }]
                                 """
                ));
    }

    @Test
    void deleteCredentials_shouldTriggerErrorMessage_whenIdDoesNotExist() throws Exception {
        mvc.perform(MockMvcRequestBuilders.delete("/api/credentials/1"))
            .andExpect(MockMvcResultMatchers.status().isNotFound())
            .andExpect(MockMvcResultMatchers.content().json("""
                                     {
                                     "message": "Credentials not found",
                                     "id": "1"
                                     }
                                     """
            ));

    }

}