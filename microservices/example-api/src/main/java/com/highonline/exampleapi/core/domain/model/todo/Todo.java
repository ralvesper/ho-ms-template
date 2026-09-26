package com.highonline.exampleapi.core.domain.model.todo;

import com.highonline.common.core.domain.model.AbstractEventSourceEntity;
import com.highonline.common.core.domain.model.DomainException;
import lombok.Getter;

import java.time.OffsetDateTime;

@Getter
public class Todo extends AbstractEventSourceEntity {

    private final TodoId id;
    private String title;
    private boolean completed;
    private final OffsetDateTime createdAt;
    private final Long version; // controle otimista, gerenciado pela persistência

    private Todo(TodoId id, String title, boolean completed, OffsetDateTime createdAt, Long version) {
        this.id = id;
        this.title = title;
        this.completed = completed;
        this.createdAt = createdAt;
        this.version = version;
    }

    public static Todo brandNew(String title) {
        requireTitle(title);
        Todo todo = new Todo(TodoId.generate(), title, false, OffsetDateTime.now(), null);
        todo.publishDomainEvent(new TodoCreatedEvent(todo.id, title, todo.createdAt));
        return todo;
    }

    public static Todo existing(TodoId id, String title, boolean completed, OffsetDateTime createdAt, Long version) {
        return new Todo(id, title, completed, createdAt, version);
    }

    public void changeTitle(String newTitle) {
        requireTitle(newTitle);
        String previous = this.title;
        this.title = newTitle;
        publishDomainEvent(new TodoTitleChangedEvent(id, previous, newTitle));
    }

    public void complete() {
        if (completed) {
            throw new DomainException("Todo " + id + " is already completed");
        }
        this.completed = true;
        publishDomainEvent(new TodoCompletedEvent(id, OffsetDateTime.now()));
    }

    public void reopen() {
        if (!completed) {
            throw new DomainException("Todo " + id + " is not completed");
        }
        this.completed = false;
        publishDomainEvent(new TodoReopenedEvent(id));
    }

    /** Registra a exclusão; o evento é publicado quando o repositório apaga o agregado. */
    public void markAsRemoved() {
        publishDomainEvent(new TodoRemovedEvent(id, title));
    }

    private static void requireTitle(String title) {
        if (title == null || title.isBlank()) {
            throw new DomainException("Todo title must not be blank");
        }
    }
}
