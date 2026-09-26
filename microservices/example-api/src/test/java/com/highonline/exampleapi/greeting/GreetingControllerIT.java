package com.highonline.exampleapi.greeting;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.postgresql.PostgreSQLContainer;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Testcontainers
class GreetingControllerIT {

    @Container
    @ServiceConnection
    static PostgreSQLContainer postgres = new PostgreSQLContainer("postgres:17-alpine");

    @Autowired
    MockMvc mvc;

    @Test
    void createsAndFetchesGreeting() throws Exception {
        mvc.perform(post("/api/v1/greetings").contentType(MediaType.APPLICATION_JSON)
                        .content("{\"message\":\"oi\"}"))
                .andExpect(status().isCreated());

        mvc.perform(get("/api/v1/greetings"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[?(@.message=='oi')]").exists());
    }

    @Test
    void rejectsBlankMessage() throws Exception {
        mvc.perform(post("/api/v1/greetings").contentType(MediaType.APPLICATION_JSON)
                        .content("{\"message\":\"\"}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void unknownIdIs404() throws Exception {
        mvc.perform(get("/api/v1/greetings/999999")).andExpect(status().isNotFound());
    }
}
