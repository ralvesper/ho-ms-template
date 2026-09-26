package com.highonline.exampleapi.infrastructure.adapters.in.web.todo;

import com.highonline.exampleapi.core.ports.in.todo.ForManagingTodos;
import com.highonline.exampleapi.core.ports.in.todo.ForQueryingTodos;
import com.highonline.exampleapi.core.ports.in.todo.TodoInput;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/** No Docker needed: the application ports are mocked, only the web layer and the shared error handler run. */
@WebMvcTest(TodoController.class)
class TodoControllerConflictTest {

    @Autowired
    MockMvc mvc;

    @MockitoBean
    ForManagingTodos forManagingTodos;

    @MockitoBean
    ForQueryingTodos forQueryingTodos;

    @Test
    void staleUpdateIsMappedTo409Conflict() throws Exception {
        doThrow(new ObjectOptimisticLockingFailureException(Object.class, "id"))
                .when(forManagingTodos).changeTitle(anyString(), any(TodoInput.class));

        mvc.perform(put("/api/v1/todos/00000000-0000-0000-0000-000000000000")
                        .contentType(MediaType.APPLICATION_JSON).content("{\"title\":\"x\"}"))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.title").value("Conflict"))
                .andExpect(jsonPath("$.type").value("/errors/conflict"));
    }
}
