package com.highonline.exampleapi.core.domain.model.todo;

import com.highonline.common.core.domain.model.DomainException;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class TodoTest {

    @Test
    void brandNewPublishesCreatedEvent() {
        Todo todo = Todo.brandNew("comprar pão");
        assertThat(todo.isCompleted()).isFalse();
        assertThat(todo.domainEvents()).singleElement().isInstanceOf(TodoCreatedEvent.class);
    }

    @Test
    void lifecycleTransitionsPublishOneEventEach() {
        Todo todo = Todo.brandNew("a");
        todo.clearDomainEvents();

        todo.changeTitle("b");
        todo.complete();
        todo.reopen();
        todo.markAsRemoved();

        assertThat(todo.domainEvents()).extracting(Object::getClass).containsExactly(
                TodoTitleChangedEvent.class, TodoCompletedEvent.class, TodoReopenedEvent.class, TodoRemovedEvent.class);
    }

    @Test
    void enforcesInvariants() {
        Todo todo = Todo.brandNew("a");
        assertThatThrownBy(todo::reopen).isInstanceOf(DomainException.class);
        todo.complete();
        assertThatThrownBy(todo::complete).isInstanceOf(DomainException.class);
        assertThatThrownBy(() -> todo.changeTitle(" ")).isInstanceOf(DomainException.class);
        assertThatThrownBy(() -> Todo.brandNew("")).isInstanceOf(DomainException.class);
    }

    @Test
    void malformedIdIsNotFound() {
        assertThatThrownBy(() -> TodoId.from("!!")).isInstanceOf(TodoNotFoundException.class);
    }
}
