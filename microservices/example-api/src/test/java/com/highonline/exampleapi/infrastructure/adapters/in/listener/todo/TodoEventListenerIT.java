package com.highonline.exampleapi.infrastructure.adapters.in.listener.todo;

import com.highonline.exampleapi.core.domain.model.todo.*;
import com.highonline.exampleapi.core.ports.in.todo.ForManagingTodos;
import com.highonline.exampleapi.core.ports.in.todo.TodoInput;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.postgresql.PostgreSQLContainer;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

/** Drives the use cases through the application port and checks the real listener bean received each event. */
@SpringBootTest
@Testcontainers
class TodoEventListenerIT {

    @Container
    @ServiceConnection
    static PostgreSQLContainer postgres = new PostgreSQLContainer("postgres:17-alpine");

    @Autowired
    ForManagingTodos forManagingTodos;

    @MockitoSpyBean
    TodoEventListener listener;

    @Test
    void listenerReceivesEachLifecycleEventWithTheAggregateId() {
        String id = forManagingTodos.create(new TodoInput("a"));
        forManagingTodos.changeTitle(id, new TodoInput("b"));
        forManagingTodos.complete(id);
        forManagingTodos.reopen(id);
        forManagingTodos.remove(id);

        var created = ArgumentCaptor.forClass(TodoCreatedEvent.class);
        verify(listener).listen(created.capture());
        assertThat(created.getValue().todoId()).hasToString(id);

        var titleChanged = ArgumentCaptor.forClass(TodoTitleChangedEvent.class);
        verify(listener).listen(titleChanged.capture());
        assertThat(titleChanged.getValue().previousTitle()).isEqualTo("a");
        assertThat(titleChanged.getValue().newTitle()).isEqualTo("b");

        verify(listener).listen(any(TodoCompletedEvent.class));
        verify(listener).listen(any(TodoReopenedEvent.class));

        var removed = ArgumentCaptor.forClass(TodoRemovedEvent.class);
        verify(listener).listen(removed.capture());
        assertThat(removed.getValue().todoId()).hasToString(id);
    }

    @Test
    void listenerIsNotCalledWhenTheTransitionIsRejected() {
        String id = forManagingTodos.create(new TodoInput("a"));

        assertThatThrownBy(() -> forManagingTodos.reopen(id)).isInstanceOf(RuntimeException.class);

        verify(listener, never()).listen(any(TodoReopenedEvent.class));
        verify(listener, never()).listen(any(TodoCompletedEvent.class));
    }
}
