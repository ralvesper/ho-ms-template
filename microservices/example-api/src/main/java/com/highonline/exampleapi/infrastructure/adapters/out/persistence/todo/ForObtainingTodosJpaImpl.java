package com.highonline.exampleapi.infrastructure.adapters.out.persistence.todo;

import com.highonline.exampleapi.core.domain.model.todo.TodoId;
import com.highonline.exampleapi.core.domain.model.todo.TodoNotFoundException;
import com.highonline.exampleapi.core.ports.in.todo.TodoOutput;
import com.highonline.exampleapi.core.ports.out.todo.ForObtainingTodos;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ForObtainingTodosJpaImpl implements ForObtainingTodos {

    private final TodoPersistenceEntityRepository repository;

    @Override
    public TodoOutput findById(TodoId id) {
        return repository.findById(id.value()).map(this::toOutput)
                .orElseThrow(() -> new TodoNotFoundException(id.toString()));
    }

    @Override
    public Page<TodoOutput> findAll(Pageable pageable) {
        return repository.findAll(pageable).map(this::toOutput);
    }

    private TodoOutput toOutput(TodoPersistenceEntity e) {
        return new TodoOutput(e.getId().toString(), e.getTitle(), e.isCompleted(), e.getCreatedAt());
    }
}
