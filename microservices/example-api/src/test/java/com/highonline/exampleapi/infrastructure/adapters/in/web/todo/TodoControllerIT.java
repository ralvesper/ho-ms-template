package com.highonline.exampleapi.infrastructure.adapters.in.web.todo;

import com.highonline.exampleapi.core.domain.model.todo.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.context.event.ApplicationEvents;
import org.springframework.test.context.event.RecordApplicationEvents;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.postgresql.PostgreSQLContainer;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Testcontainers
@RecordApplicationEvents
class TodoControllerIT {

    @Container
    @ServiceConnection
    static PostgreSQLContainer postgres = new PostgreSQLContainer("postgres:17-alpine");

    @Autowired
    MockMvc mvc;

    @Autowired
    ApplicationEvents events;

    private String create(String title) throws Exception {
        String body = mvc.perform(post("/api/v1/todos").contentType(MediaType.APPLICATION_JSON)
                        .content("{\"title\":\"" + title + "\"}"))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();
        return body.replaceAll(".*\"id\":\"([^\"]+)\".*", "$1");
    }

    @Test
    void fullLifecyclePublishesOneEventPerStep() throws Exception {
        String id = create("comprar pão");

        mvc.perform(put("/api/v1/todos/" + id).contentType(MediaType.APPLICATION_JSON)
                        .content("{\"title\":\"comprar leite\"}"))
                .andExpect(status().isOk()).andExpect(jsonPath("$.title").value("comprar leite"));
        mvc.perform(post("/api/v1/todos/" + id + "/complete"))
                .andExpect(status().isOk()).andExpect(jsonPath("$.completed").value(true));
        mvc.perform(post("/api/v1/todos/" + id + "/reopen"))
                .andExpect(status().isOk()).andExpect(jsonPath("$.completed").value(false));
        mvc.perform(delete("/api/v1/todos/" + id)).andExpect(status().isNoContent());
        mvc.perform(get("/api/v1/todos/" + id)).andExpect(status().isNotFound());

        assertThat(events.stream(TodoCreatedEvent.class)).hasSize(1);
        assertThat(events.stream(TodoTitleChangedEvent.class)).hasSize(1);
        assertThat(events.stream(TodoCompletedEvent.class)).hasSize(1);
        assertThat(events.stream(TodoReopenedEvent.class)).hasSize(1);
        assertThat(events.stream(TodoRemovedEvent.class)).hasSize(1);
    }

    @Test
    void invalidTransitionIs422AndPublishesNothing() throws Exception {
        String id = create("a");
        long before = events.stream(TodoCompletedEvent.class).count();

        mvc.perform(post("/api/v1/todos/" + id + "/reopen")).andExpect(status().isUnprocessableEntity());
        mvc.perform(post("/api/v1/todos/" + id + "/complete")).andExpect(status().isOk());
        mvc.perform(post("/api/v1/todos/" + id + "/complete")).andExpect(status().isUnprocessableEntity());

        assertThat(events.stream(TodoCompletedEvent.class).count()).isEqualTo(before + 1);
        assertThat(events.stream(TodoReopenedEvent.class)).isEmpty();
    }

    @Test
    void unknownIdIs404AndBlankTitleIs400() throws Exception {
        String unknown = "/api/v1/todos/00000000-0000-0000-0000-000000000000";
        mvc.perform(delete(unknown)).andExpect(status().isNotFound());
        mvc.perform(post("/api/v1/todos").contentType(MediaType.APPLICATION_JSON).content("{\"title\":\"\"}"))
                .andExpect(status().isBadRequest());
    }
}
