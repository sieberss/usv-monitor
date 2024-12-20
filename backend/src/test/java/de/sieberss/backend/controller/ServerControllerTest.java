package de.sieberss.backend.controller;

import de.sieberss.backend.model.Credentials;
import de.sieberss.backend.model.CredentialsWithoutEncryption;
import de.sieberss.backend.model.Server;
import de.sieberss.backend.model.Ups;
import de.sieberss.backend.repo.CredentialsRepo;
import de.sieberss.backend.repo.ServerRepo;
import de.sieberss.backend.repo.UpsRepo;
import de.sieberss.backend.utils.EncryptionService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.stream.Stream;

@SpringBootTest
@WithMockUser
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@AutoConfigureMockMvc
class ServerControllerTest {

    @Autowired
    private MockMvc mvc;
    @Autowired
    private ServerRepo serverRepo;
    @Autowired
    private UpsRepo upsRepo;
    @Autowired
    private CredentialsRepo credentialsRepo;

    @BeforeAll
    static void setUp() throws Exception {
        EncryptionService.setTestKey();
    }

    static Stream<String> missingUserNameOrPasswordJson(){
        return Stream.of(
                """
                                 {
                                     "name": "Test-server",
                                     "address": "1.1.1.1",
                                     "user": "",
                                     "password": "password",
                                     "upsId": "1",
                                     "shutdownTime" : 180
                                 }
                                """,
                """
                                 {
                                     "name": "Test-server",
                                     "address": "1.1.1.1",
                                     "user": "",
                                     "password": "",
                                     "upsId": "1",
                                     "shutdownTime" : 180
                                 }
                                """,
                """
                                 {
                                     "name": "Test-server",
                                     "address": "1.1.1.1",
                                     "user": "",
                                     "password": null,
                                     "upsId": "1",
                                     "shutdownTime" : 180
                                 }
                                """,
                """
                                 {
                                     "name": "Test-server",
                                     "address": "1.1.1.1",
                                     "user": null,
                                     "password": "password",
                                     "upsId": "1",
                                     "shutdownTime" : 180
                                 }
                                """,
                """
                                 {
                                     "name": "Test-server",
                                     "address": "1.1.1.1",
                                     "user": null,
                                     "password": "",
                                     "upsId": "1",
                                     "shutdownTime" : 180
                                 }
                                """,
                """
                                 {
                                     "name": "Test-server",
                                     "address": "1.1.1.1",
                                     "user": null,
                                     "password": null,
                                     "upsId": "1",
                                     "shutdownTime" : 180
                                 }
                                """,
                """
                                 {
                                     "name": "Test-server",
                                     "address": "1.1.1.1",
                                     "user": "user",
                                     "password": "",
                                     "upsId": "1",
                                     "shutdownTime" : 180
                                 }
                                """,
                """
                                 {
                                     "name": "Test-server",
                                     "address": "1.1.1.1",
                                     "user": "user",
                                     "password": null,
                                     "upsId": "1",
                                     "shutdownTime" : 180
                                 }
                                """
        );
    }
    @Test
    void getServerDTOList_shouldReturnListWithOneObject_whenOneObjectWasSavedInRepository() throws Exception {
        Ups ups = new Ups("1", "Test-UPS", "192.168.1.1", "");
        upsRepo.save(ups);
        CredentialsWithoutEncryption decrypted = new CredentialsWithoutEncryption("u","user", "password", true);
        Credentials encrypted = EncryptionService.encryptCredentials(decrypted);
        credentialsRepo.save(encrypted);
        Server server = new Server("22", "Test-server", "1.1.1.1", encrypted, ups, 180);
        serverRepo.save(server);
        mvc.perform(MockMvcRequestBuilders.get("/api/server"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(
                        """
                                [
                                 {
                                     "id": "22",
                                     "name": "Test-server",
                                     "address": "1.1.1.1",
                                     "credentials": {
                                          "id" : "u",
                                          "user": "user",
                                          "password": "password",
                                          "global": true
                                     },
                                     "upsId": "1",
                                     "shutdownTime" : 180
                                 }
                                ]
                                """
                ));
    }

    @Test
    void getServerDTOList_shouldReturnEmptyList_initially() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get("/api/server"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(
                        """
                                [
                             
                                ]
                                """
                ));
    }

    @Test
    void getServerDTOById_shouldReturnServerDTO_whenIdExists() throws Exception {
        Ups ups = new Ups("1", "Test-UPS", "192.168.1.1", "");
        upsRepo.save(ups);
        CredentialsWithoutEncryption decrypted = new CredentialsWithoutEncryption("u","user", "password", true);
        Credentials encrypted = EncryptionService.encryptCredentials(decrypted);
        credentialsRepo.save(encrypted);
        Server server = new Server("22", "Test-server", "1.1.1.1", encrypted, ups, 180);
        serverRepo.save(server);
        mvc.perform(MockMvcRequestBuilders.get("/api/server/22"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(
                        """
                                 {
                                     "id": "22",
                                     "name": "Test-server",
                                     "address": "1.1.1.1",
                                     "credentials": {
                                          "id" : "u",
                                          "user": "user",
                                          "password": "password",
                                          "global": true
                                     },
                                     "upsId": "1",
                                     "shutdownTime" : 180
                                 }
                                """
                ));
    }

    @Test
    void getServerDTOById_shouldTriggerErrorMessage_whenIdDoesNotExist() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get("/api/server/1"))
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.content().json("""
                                     {
                                     "message": "Server not found",
                                     "id": "1"
                                     }
                                     """
                ));

    }

    @Test
    void createServer_shouldReturnSubmittedObjectWithNewId() throws Exception {
        Ups ups = new Ups("1", "Test-UPS", "192.168.1.1", "");
        upsRepo.save(ups);
        CredentialsWithoutEncryption decrypted = new CredentialsWithoutEncryption("u","user", "password", true);
        Credentials encrypted = EncryptionService.encryptCredentials(decrypted);
        credentialsRepo.save(encrypted);
        mvc.perform(MockMvcRequestBuilders.post("/api/server")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(
                                """
                                 {
                                     "name": "Test-server",
                                     "address": "1.1.1.1",
                                     "credentials": {
                                          "id" : "u",
                                          "user": "user",
                                          "password": "password",
                                          "global": true
                                     },
                                     "upsId": "1",
                                     "shutdownTime" : 180
                                 }
                                """
                        ))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").isNotEmpty())
                .andExpect(MockMvcResultMatchers.content().json(
                        """
                                 {
                                     "name": "Test-server",
                                     "address": "1.1.1.1",
                                     "credentials": {
                                          "id" : "u",
                                          "user": "user",
                                          "password": "password",
                                          "global": true
                                     },
                                     "upsId": "1",
                                     "shutdownTime" : 180
                                 }
                                """
                ));    }

    @Test
    void createServer_shouldReturnSubmittedObjectWithNewId_andEmptyUpsIdW_whenUpsIdNotInDatabase() throws Exception {
        CredentialsWithoutEncryption decrypted = new CredentialsWithoutEncryption("u","user", "password", true);
        Credentials encrypted = EncryptionService.encryptCredentials(decrypted);
        credentialsRepo.save(encrypted);
        mvc.perform(MockMvcRequestBuilders.post("/api/server")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(
                                """
                                 {
                                     "name": "Test-server",
                                     "address": "1.1.1.1",
                                     "credentials": {
                                          "id" : "u",
                                          "user": "user",
                                          "password": "password",
                                          "global": true
                                     },
                                     "upsId": "1",
                                     "shutdownTime" : 180
                                 }
                                """
                        ))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").isNotEmpty())
                .andExpect(MockMvcResultMatchers.content().json(
                        """
                                 {
                                     "name": "Test-server",
                                     "address": "1.1.1.1",
                                     "credentials": {
                                          "id" : "u",
                                          "user": "user",
                                          "password": "password",
                                          "global": true
                                     },
                                     "upsId": "",
                                     "shutdownTime" : 180
                                 }
                                """
                ));    }

    @Test
    void updateServer_shouldUpdateServer_whenIdExists() throws Exception {
        Ups ups = new Ups("1", "Test-UPS", "192.168.1.1", "");
        upsRepo.save(ups);
        CredentialsWithoutEncryption decrypted = new CredentialsWithoutEncryption("u","user", "password", true);
        Credentials encrypted = EncryptionService.encryptCredentials(decrypted);
        credentialsRepo.save(encrypted);
        serverRepo.save(new Server("22", "unnamed", "", null, null, 180));
        mvc.perform(MockMvcRequestBuilders.put("/api/server/22")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                 {
                                     "name": "Test-server",
                                     "address": "1.1.1.1",
                                     "credentials": {
                                          "id" : "u",
                                          "user": "user",
                                          "password": "password",
                                          "global": true
                                     },
                                     "upsId": "1",
                                     "shutdownTime" : 180
                                 }
                                """))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").isNotEmpty())
                .andExpect(MockMvcResultMatchers.content().json("""
                             {
                                     "id": "22",
                                     "name": "Test-server",
                                     "address": "1.1.1.1",
                                     "credentials": {
                                          "id" : "u",
                                          "user": "user",
                                          "password": "password",
                                          "global": true
                                     },
                                     "upsId": "1",
                                     "shutdownTime" : 180
                                 }
                             """));
    }

    @Test
    void updateServer_shouldUpdateServer_withEmptyUpsId_whenIdExists_butUpsIdNotInDatabase() throws Exception {
        CredentialsWithoutEncryption decrypted = new CredentialsWithoutEncryption("u","user", "password", true);
        Credentials encrypted = EncryptionService.encryptCredentials(decrypted);
        credentialsRepo.save(encrypted);
        serverRepo.save(new Server("22", "unnamed", "", null, null, 180));
        mvc.perform(MockMvcRequestBuilders.put("/api/server/22")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                 {
                                     "name": "Test-server",
                                     "address": "1.1.1.1",
                                     "credentials": {
                                          "id" : "u",
                                          "user": "user",
                                          "password": "password",
                                          "global": true
                                     },
                                     "upsId": "1",
                                     "shutdownTime" : 180
                                 }
                                """))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").isNotEmpty())
                .andExpect(MockMvcResultMatchers.content().json("""
                             {
                                     "id": "22",
                                     "name": "Test-server",
                                     "address": "1.1.1.1",
                                     "credentials": {
                                          "id" : "u",
                                          "user": "user",
                                          "password": "password",
                                          "global": true
                                     },
                                     "upsId": "",
                                     "shutdownTime" : 180
                                 }
                             """));
    }

    @Test
    void updateServer_shouldTriggerErrorMessage_whenIdDoesNotExist() throws Exception {
        mvc.perform(MockMvcRequestBuilders.put("/api/server/22")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                                 {
                                     "name": "Test-server",
                                     "address": "1.1.1.1",
                                     "credentials": {
                                          "id" : "u",
                                          "user": "user",
                                          "password": "password",
                                          "global": true
                                     },
                                     "upsId": "1",
                                     "shutdownTime" : 180
                                 }
                          """))
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.content().json("""
                                     {
                                     "message": "Server not found",
                                     "id": "22"
                                     }
                                     """
                ));

    }

    @Test
    void deleteServer_shouldDeleteServer_whenIdExists() throws Exception {
        CredentialsWithoutEncryption decrypted = new CredentialsWithoutEncryption("u","user", "password", true);
        Credentials encrypted = EncryptionService.encryptCredentials(decrypted);
        credentialsRepo.save(encrypted);
        serverRepo.save(new Server("22", "Test-server", "1.1.1.1", encrypted, null, 180));
        serverRepo.save(new Server("33", "unnamed", "", null, null, 180));
        mvc.perform(MockMvcRequestBuilders.delete("/api/server/33"))
            .andExpect(MockMvcResultMatchers.status().isOk());
        mvc.perform(MockMvcRequestBuilders.get("/api/server"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json("""
                               [
                                 {
                                     "id": "22",
                                     "name": "Test-server",
                                     "address": "1.1.1.1",
                                     "credentials": {
                                          "id" : "u",
                                          "user": "user",
                                          "password": "password",
                                          "global": true
                                     },
                                     "upsId": "",
                                     "shutdownTime" : 180
                                 }
                               ]
                               """
                ));
    }

    @Test
    void deleteServer_shouldTriggerErrorMessage_whenIdDoesNotExist() throws Exception {
        mvc.perform(MockMvcRequestBuilders.delete("/api/server/1"))
            .andExpect(MockMvcResultMatchers.status().isNotFound())
            .andExpect(MockMvcResultMatchers.content().json("""
                                     {
                                     "message": "Server not found",
                                     "id": "1"
                                     }
                                     """
            ));

    }

    @Test
    void createServerWithNewLocalCredentials_shouldCreateServer_withSubmittedData() throws Exception {
        Ups ups = new Ups("1", "Test-UPS", "192.168.1.1", "");
        upsRepo.save(ups);
        mvc.perform(MockMvcRequestBuilders.post("/api/server/localcredentials")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(
                                """
                                 {
                                     "name": "Test-server",
                                     "address": "1.1.1.1",
                                     "user": "user",
                                     "password": "password",
                                     "upsId": "1",
                                     "shutdownTime" : 180
                                 }
                                """
                        ))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").isNotEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.credentials.id").isNotEmpty())
                .andExpect(MockMvcResultMatchers.content().json(
                        """
                                 {
                                     "name": "Test-server",
                                     "address": "1.1.1.1",
                                     "credentials": {
                                          "user": "user",
                                          "password": "password",
                                          "global": false
                                     },
                                     "upsId": "1",
                                     "shutdownTime" : 180
                                 }
                                """
                ));
    }

    @ParameterizedTest
    @MethodSource("missingUserNameOrPasswordJson")
    void createServerWithNewLocalCredentials_shouldTriggerErrorMessage_whenUsernameOrPasswordEmptyOrNull(String requestContent) throws Exception {
        Ups ups = new Ups("1", "Test-UPS", "192.168.1.1", "");
        upsRepo.save(ups);
        mvc.perform(MockMvcRequestBuilders.post("/api/server/localcredentials")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestContent))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.content().json(
                        """
                                 {
                                     "message": "Illegal argument",
                                     "id": "Missing username and/or password"
                                 }
                                """
                ));
    }

    @Test
    void updateServerWithNewLocalCredentials_shouldUpdateServer() throws Exception {
        Ups ups = new Ups("1", "Test-UPS", "192.168.1.1", "");
        upsRepo.save(ups);
        serverRepo.save(new Server("22", "unnamed", "", null, null, 180));
        mvc.perform(MockMvcRequestBuilders.put("/api/server/localcredentials/22")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                 {
                                     "name": "Test-server",
                                     "address": "1.1.1.1",
                                     "user": "user",
                                     "password": "password",
                                     "global": false,
                                     "upsId": "1",
                                     "shutdownTime" : 180
                                 }
                                """))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.credentials.id").isNotEmpty())
                .andExpect(MockMvcResultMatchers.content().json("""
                             {
                                     "id": "22",
                                     "name": "Test-server",
                                     "address": "1.1.1.1",
                                     "credentials": {
                                          "user": "user",
                                          "password": "password",
                                          "global": false
                                     },
                                     "upsId": "1",
                                     "shutdownTime" : 180
                                 }
                             """));
    }


    @ParameterizedTest
    @MethodSource("missingUserNameOrPasswordJson")
    void updateServerWithNewLocalCredentials_shouldTriggerErrorMessage_whenUsernameOrPasswordEmptyOrNull(String requestContent) throws Exception {
        Ups ups = new Ups("1", "Test-UPS", "192.168.1.1", "");
        upsRepo.save(ups);
        serverRepo.save(new Server("22", "unnamed", "", null, null, 180));
        mvc.perform(MockMvcRequestBuilders.put("/api/server/localcredentials/22")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestContent))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.content().json(
                        """
                                 {
                                     "message": "Illegal argument",
                                     "id": "Missing username and/or password"
                                 }
                                """
                ));
    }

}