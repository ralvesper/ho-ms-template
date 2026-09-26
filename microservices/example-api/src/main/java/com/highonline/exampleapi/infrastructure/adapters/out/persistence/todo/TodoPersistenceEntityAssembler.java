package com.highonline.exampleapi.infrastructure.adapters.out.persistence.todo;

import com.highonline.exampleapi.core.domain.model.todo.Todo;
import org.springframework.stereotype.Component;

/** Domínio -> entity JPA. */
@Component
public class TodoPersistenceEntityAssembler {

    public TodoPersistenceEntity fromDomain(Todo todo) {
        return merge(new TodoPersistenceEntity(), todo);
    }

    public TodoPersistenceEntity merge(TodoPersistenceEntity entity, Todo todo) {
        entity.setId(todo.getId().value());
        entity.setTitle(todo.getTitle());
        entity.setCompleted(todo.isCompleted());
        entity.setCreatedAt(todo.getCreatedAt());
        entity.setVersion(todo.getVersion());
        entity.addEvents(todo.domainEvents());
        return entity;
    }
}
