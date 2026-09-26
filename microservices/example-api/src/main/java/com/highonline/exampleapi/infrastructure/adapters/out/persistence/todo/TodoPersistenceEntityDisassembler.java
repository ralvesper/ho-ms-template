package com.highonline.exampleapi.infrastructure.adapters.out.persistence.todo;

import com.highonline.exampleapi.core.domain.model.todo.Todo;
import com.highonline.exampleapi.core.domain.model.todo.TodoId;
import org.springframework.stereotype.Component;

/** Entity JPA -> domínio. */
@Component
public class TodoPersistenceEntityDisassembler {

    public Todo toDomainEntity(TodoPersistenceEntity entity) {
        return Todo.existing(new TodoId(entity.getId()), entity.getTitle(), entity.isCompleted(),
                entity.getCreatedAt(), entity.getVersion());
    }
}
