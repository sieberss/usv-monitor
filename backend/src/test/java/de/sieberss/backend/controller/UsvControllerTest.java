package de.sieberss.backend.controller;

import de.sieberss.backend.model.Usv;
import de.sieberss.backend.model.UsvRepo;
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
class UsvControllerTest {

    @Autowired
    private MockMvc mvc;
    @Autowired
    private UsvRepo repo;

    @Test
    void getAllUsvs_shouldReturnListWithOneObject_whenOneObjectWasSavedInRepository() throws Exception {
        Usv usv = new Usv("1", "Test-USV", "192.168.1.1", "");
        repo.save(usv);
        mvc.perform(MockMvcRequestBuilders.get("/api/usv"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(
                        """
                                [
                                 {
                                     "name": "Test-USV",
                                     "id": "1",
                                     "address": "192.168.1.1",
                                     "community": ""
                                 }
                                ]
                                """
                ));
    }

    @Test
    void getAllUsvs_shouldReturnEmptyList_initially() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get("/api/usv"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(
                        """
                                [
                             
                                ]
                                """
                ));
    }

    @Test
    void getUsvById_shouldReturnUsv_whenIdExists() throws Exception {
        Usv usv = new Usv("1", "Test-USV", "192.168.1.1", "");
        repo.save(usv);
        mvc.perform(MockMvcRequestBuilders.get("/api/usv/1"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json("""
                                 {
                                     "name": "Test-USV",
                                     "id": "1",
                                     "address": "192.168.1.1",
                                     "community": ""
                                 }
                                """
                ));
    }

    @Test
    void getUsvById_shouldTriggerErrorMessage_whenIdDoesNotExist() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get("/api/usv/1"))
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.content().json("""
                                     {
                                     "message": "Not found",
                                     "id": "1"
                                     }
                                     """
                ));

    }

    @Test
    void createUsv_shouldReturnSubmittedObjectWithNewId() throws Exception {
        mvc.perform(MockMvcRequestBuilders.post("/api/usv")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                                 {
                                     "name": "Test-USV",
                                     "address": "192.168.1.1",
                                     "community": "com"
                                 }
                          """))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").isNotEmpty())
                .andExpect(MockMvcResultMatchers.content().json("""
                             {
                                     "name": "Test-USV",
                                     "address": "192.168.1.1",
                                     "community": "com"
                             }
                             """));
    }

    @Test
    void updateUsv_shouldUpdateUsv_whenIdExists() throws Exception {
        repo.save(new Usv("1", "Test-USV", "192.168.1.1", ""));
        mvc.perform(MockMvcRequestBuilders.put("/api/usv/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                 {
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
}