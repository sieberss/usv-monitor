package de.sieberss.backend.controller;

import de.sieberss.backend.model.Server;
import de.sieberss.backend.model.Ups;
import de.sieberss.backend.repo.ServerRepo;
import de.sieberss.backend.repo.UpsRepo;
import org.junit.jupiter.api.BeforeEach;
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
@AutoConfigureMockMvc
@DirtiesContext
class MonitorControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private UpsRepo upsRepo;
    @Autowired
    private ServerRepo serverRepo;

    @BeforeEach
    void setUp() {
        upsRepo.deleteAll();
        serverRepo.deleteAll();
        Ups ups1 = new Ups("1", "USV1", "192.168.1.1", "c");
        Ups ups2 = new Ups("2", "USV2", "192.168.1.2", "c");
        upsRepo.save(ups1);
        upsRepo.save(ups2);
        serverRepo.save(new Server("11", "Server1", "192.168.1.11", null, ups1, 120));
        serverRepo.save(new Server("22", "Server2", "192.168.1.22", null, ups2, 120));
        serverRepo.save(new Server("33", "Server3", "192.168.1.33", null, ups1, 120));
    }

    @Test
    void getAllStatuses_shouldReturnAllStatuses_whenMonitoring() throws Exception {
        //set monitoring true before calling getAllStatuses
        mockMvc.perform(MockMvcRequestBuilders.post("/api/monitor")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("true"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string("true"));
        mockMvc.perform(MockMvcRequestBuilders.get("/api/monitor"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json("""
                    {
                        "monitoring": true,
                        "statusMap": {
                            "1": {"upsOrServerId": "1"},
                            "2": {"upsOrServerId": "2"},
                            "11": {"upsOrServerId": "11"},
                            "22": {"upsOrServerId": "22"},
                            "33": {"upsOrServerId": "33"}
                        }
                    }
                    """));
    }

    @Test
    void getAllStatuses_shouldReturnEmptyMap_whenNotMonitoring() throws Exception {
        //set monitoring true before calling getAllStatuses
        mockMvc.perform(MockMvcRequestBuilders.post("/api/monitor")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("false"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string("false"));
        mockMvc.perform(MockMvcRequestBuilders.get("/api/monitor"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json("""
                    {
                        "monitoring": false,
                        "statusMap": {
                        }
                    }
                    """));
    }

    @Test
    void changeMode_shouldReturnTrueWhenTrueIsSubmitted() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/api/monitor")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("true"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string("true"));
    }

    @Test
    void changeMode_shouldReturnFalseWhenFalseIsSubmitted() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/api/monitor")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("false"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string("false"));
    }
}