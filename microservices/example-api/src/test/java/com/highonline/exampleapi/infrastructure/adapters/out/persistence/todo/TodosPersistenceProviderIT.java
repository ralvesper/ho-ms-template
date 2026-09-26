package com.highonline.exampleapi.infrastructure.adapters.out.persistence.todo;

import com.highonline.exampleapi.core.domain.model.todo.Todo;
import com.highonline.exampleapi.core.domain.model.todo.Todos;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.postgresql.PostgreSQLContainer;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@Testcontainers
class TodosPersistenceProviderIT {

    @Container
    @ServiceConnection
    static PostgreSQLContainer postgres = new PostgreSQLContainer("postgres:17-alpine");

    @Autowired
    Todos todos;

    @Test
    void addThenOfIdRoundTripsThroughAssemblerAndDisassembler() {
        Todo todo = Todo.brandNew("comprar pão");
        todos.add(todo);

        Todo loaded = todos.ofId(todo.getId()).orElseThrow();

        assertThat(loaded.getId()).isEqualTo(todo.getId());
        assertThat(loaded.getTitle()).isEqualTo("comprar pão");
        assertThat(loaded.isCompleted()).isFalse();
        assertThat(loaded.getCreatedAt()).isCloseTo(todo.getCreatedAt(), org.assertj.core.api.Assertions.within(1, java.time.temporal.ChronoUnit.MILLIS));
        assertThat(loaded.getVersion()).isZero();
        assertThat(loaded.domainEvents()).isEmpty();
        assertThat(todo.domainEvents()).as("provider clears the aggregate's events after saving").isEmpty();
    }

    @Test
    void addOnExistingAggregateMergesIntoTheSameRowAndBumpsVersion() {
        Todo todo = Todo.brandNew("a");
        todos.add(todo);

        Todo loaded = todos.ofId(todo.getId()).orElseThrow();
        loaded.changeTitle("b");
        loaded.complete();
        todos.add(loaded);

        Todo reloaded = todos.ofId(todo.getId()).orElseThrow();
        assertThat(reloaded.getTitle()).isEqualTo("b");
        assertThat(reloaded.isCompleted()).isTrue();
        assertThat(reloaded.getVersion()).isEqualTo(1L);
    }

    @Test
    void removeDeletesTheRow() {
        Todo todo = Todo.brandNew("a");
        todos.add(todo);

        Todo loaded = todos.ofId(todo.getId()).orElseThrow();
        loaded.markAsRemoved();
        todos.remove(loaded);

        assertThat(todos.ofId(todo.getId())).isEmpty();
    }

    @Test
    void staleUpdateIsRejectedByOptimisticLocking() {
        Todo todo = Todo.brandNew("a");
        todos.add(todo);

        Todo first = todos.ofId(todo.getId()).orElseThrow();
        Todo second = todos.ofId(todo.getId()).orElseThrow(); // same version as "first"

        first.changeTitle("from first");
        todos.add(first);

        second.changeTitle("from second");
        assertThatThrownBy(() -> todos.add(second)).isInstanceOf(ObjectOptimisticLockingFailureException.class);

        assertThat(todos.ofId(todo.getId()).orElseThrow().getTitle()).isEqualTo("from first");
    }
}
