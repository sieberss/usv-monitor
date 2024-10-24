package de.sieberss.backend.controller;

import de.sieberss.backend.model.CredentialsWithoutEncryption;
import de.sieberss.backend.repo.CredentialsRepo;
import de.sieberss.backend.utils.EncryptionService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@AutoConfigureMockMvc
class CredentialsControllerTest {

    @Autowired
    private MockMvc mvc;
    @Autowired
    private CredentialsRepo repo;
    @Autowired
    private EncryptionService encryptionService;


    @Test
    void getCredentialsList_shouldReturnListWithOneObject_whenOneObjectWasSavedInRepository() throws Exception {
        CredentialsWithoutEncryption decrypted = new CredentialsWithoutEncryption("1", "user", "password", true);
        repo.save(encryptionService.encryptCredentials(decrypted));
        mvc.perform(MockMvcRequestBuilders.get("/api/credentials"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(
                        """
                                [
                                 {
                                     "id": "1",
                                     "user": "user",
                                     "password": "password",
                                     "localOnly": true
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
        repo.save(encryptionService.encryptCredentials(decrypted));
        mvc.perform(MockMvcRequestBuilders.get("/api/credentials/1"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json("""
                                {
                                     "id": "1",
                                     "user": "user",
                                     "password": "password",
                                     "localOnly": true
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
                                     "localOnly": true
                                 }
                          """))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").isNotEmpty())
                .andExpect(MockMvcResultMatchers.content().json("""
                                 {
                                     "user": "user",
                                     "password": "password",
                                     "localOnly": true
                                 }
                             """));
    }

    @Test
    void updateUps_shouldUpdateCredentials_whenIdExists() throws Exception {
        CredentialsWithoutEncryption decrypted = new CredentialsWithoutEncryption("1", "user", "password", true);
        repo.save(encryptionService.encryptCredentials(decrypted));
        mvc.perform(MockMvcRequestBuilders.put("/api/credentials/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                 {
                                     "id": "1jkgjdkrte",
                                     "user": "user",
                                     "password": "password",
                                     "localOnly": true
                                 }
                          """))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").isNotEmpty())
                .andExpect(MockMvcResultMatchers.content().json("""
                             {
                                     "id": "1",
                                     "user": "user",
                                     "password": "password",
                                     "localOnly": true
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
                                     "localOnly": true
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
        repo.save(encryptionService.encryptCredentials(decrypted1));
        repo.save(encryptionService.encryptCredentials(decrypted2));
        mvc.perform(MockMvcRequestBuilders.delete("/api/credentials/2"))
            .andExpect(MockMvcResultMatchers.status().isOk());
        mvc.perform(MockMvcRequestBuilders.get("/api/credentials"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json("""
                                  [{
                                     "id": "1",
                                     "user": "user",
                                     "password": "password",
                                     "localOnly": true
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