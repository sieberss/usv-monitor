package de.sieberss.backend.controller;

import de.sieberss.backend.model.Ups;
import de.sieberss.backend.repo.UpsRepo;
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
class UpsControllerTest {

    @Autowired
    private MockMvc mvc;
    @Autowired
    private UpsRepo repo;

    @Test
    void getAllUpss_shouldReturnListWithOneObject_whenOneObjectWasSavedInRepository() throws Exception {
        Ups ups = new Ups("1", "Test-UPS", "192.168.1.1", "");
        repo.save(ups);
        mvc.perform(MockMvcRequestBuilders.get("/api/ups"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(
                        """
                                [
                                 {
                                     "name": "Test-UPS",
                                     "address": "192.168.1.1",
                                     "community": ""
                                 }
                                ]
                                """
                ));
    }

    @Test
    void getAllUpss_shouldReturnEmptyList_initially() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get("/api/ups"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(
                        """
                                [
                             
                                ]
                                """
                ));
    }

    @Test
    void getUpsById_shouldReturnUps_whenIdExists() throws Exception {
        Ups ups = new Ups("1", "Test-UPS", "192.168.1.1", "");
        repo.save(ups);
        mvc.perform(MockMvcRequestBuilders.get("/api/ups/1"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json("""
                                 {
                                     "name": "Test-UPS",
                                     "id": "1",
                                     "address": "192.168.1.1",
                                     "community": ""
                                 }
                                """
                ));
    }

    @Test
    void getUpsById_shouldTriggerErrorMessage_whenIdDoesNotExist() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get("/api/ups/1"))
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.content().json("""
                                     {
                                     "message": "UPS not found",
                                     "id": "1"
                                     }
                                     """
                ));

    }

    @Test
    void createUps_shouldReturnSubmittedObjectWithNewId() throws Exception {
        mvc.perform(MockMvcRequestBuilders.post("/api/ups")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                                 {
                                     "name": "Test-UPS",
                                     "address": "192.168.1.1",
                                     "community": "com"
                                 }
                          """))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").isNotEmpty())
                .andExpect(MockMvcResultMatchers.content().json("""
                             {
                                     "name": "Test-UPS",
                                     "address": "192.168.1.1",
                                     "community": "com"
                             }
                             """));
    }

    @Test
    void updateUps_shouldUpdateUps_whenIdExists() throws Exception {
        repo.save(new Ups("1", "Test-UPS", "192.168.1.1", ""));
        mvc.perform(MockMvcRequestBuilders.put("/api/ups/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                 {
                                     "id": "1",
                                     "name": "Test",
                                     "address": "192.168.1.2",
                                     "community": "com"
                                 }
                          """))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").isNotEmpty())
                .andExpect(MockMvcResultMatchers.content().json("""
                             {
                                     "name": "Test",
                                     "address": "192.168.1.2",
                                     "community": "com",
                                     "id": "1"
                             }
                             """));
    }

    @Test
    void updateUps_shouldTriggerErrorMessage_whenIdDoesNotExist() throws Exception {
        mvc.perform(MockMvcRequestBuilders.put("/api/ups/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                                 {
                                     "name": "Test",
                                     "address": "192.168.1.2",
                                     "community": "com"
                                 }
                          """))
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.content().json("""
                                     {
                                     "message": "UPS not found",
                                     "id": "1"
                                     }
                                     """
                ));

    }

    @Test
    void deleteUps_shouldDeleteUps_whenIdExists() throws Exception {
        repo.save(new Ups("1", "Test-UPS", "192.168.1.1", ""));
        repo.save(new Ups("2", "Test 2", "192.168.1.2", "com"));
        mvc.perform(MockMvcRequestBuilders.delete("/api/ups/1"))
            .andExpect(MockMvcResultMatchers.status().isOk());
        mvc.perform(MockMvcRequestBuilders.get("/api/ups"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json("""
                                  [{
                                     "name": "Test 2",
                                     "address": "192.168.1.2",
                                     "community": "com",
                                     "id": "2"
                                  }]
                                  """
                ));
    }

    @Test
    void deleteUps_shouldTriggerErrorMessage_whenIdDoesNotExist() throws Exception {
        mvc.perform(MockMvcRequestBuilders.delete("/api/ups/1"))
            .andExpect(MockMvcResultMatchers.status().isNotFound())
            .andExpect(MockMvcResultMatchers.content().json("""
                                     {
                                     "message": "UPS not found",
                                     "id": "1"
                                     }
                                     """
            ));

    }

}