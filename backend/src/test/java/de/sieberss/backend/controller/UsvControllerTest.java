package de.sieberss.backend.controller;

import de.sieberss.backend.model.Usv;
import de.sieberss.backend.model.UsvRepo;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@SpringBootTest
@DirtiesContext
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
    void getUsvById_shouldReturnUsv_whenOneObjectWasSavedInRepository() throws Exception {
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
    void getUsvById_shouldTriggerErrorMessage_whenTwoObjectWasSavedInRepository() throws Exception {
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
}