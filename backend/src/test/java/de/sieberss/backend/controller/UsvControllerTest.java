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

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@DirtiesContext
@AutoConfigureMockMvc
class UsvControllerTest {

    @Autowired
    private MockMvc mvc;
    @Autowired
    private UsvRepo repo;

    @Test
    void getAllUss_shouldReturnListWithOneObject_whenOneObjectWasSavedInRepository() throws Exception {
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
    void getAllUss_shouldReturnEmptyList_initially() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get("/api/usv"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(
                        """
                                [
                             
                                ]
                                """
                ));
    }

}