package com.highonline.exampleapi.infrastructure.adapters.out.persistence.todo;

import com.highonline.exampleapi.core.domain.model.todo.Todo;
import com.highonline.exampleapi.core.domain.model.todo.TodoId;
import com.highonline.exampleapi.core.domain.model.todo.TodoNotFoundException;
import com.highonline.exampleapi.core.domain.model.todo.Todos;
import lombok.RequiredArgsConstructor;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class TodosPersistenceProvider implements Todos {

    private final TodoPersistenceEntityRepository repository;
    private final TodoPersistenceEntityAssembler assembler;
    private final TodoPersistenceEntityDisassembler disassembler;

    @Transactional(readOnly = true)
    @Override
    public Optional<Todo> ofId(TodoId id) {
        return repository.findById(id.value()).map(disassembler::toDomainEntity);
    }

    @Transactional
    @Override
    public void add(Todo todo) {
        TodoPersistenceEntity entity = repository.findById(todo.getId().value())
                .map(existing -> assembler.merge(requireSameVersion(existing, todo), todo))
                .orElseGet(() -> assembler.fromDomain(todo));
        repository.save(entity);
        todo.clearDomainEvents();
    }

    /** O Spring Data também publica os eventos registrados na entity no delete. */
    @Transactional
    @Override
    public void remove(Todo todo) {
        TodoPersistenceEntity entity = repository.findById(todo.getId().value())
                .map(existing -> assembler.merge(requireSameVersion(existing, todo), todo))
                .orElseThrow(() -> new TodoNotFoundException(todo.getId().toString()));
        repository.delete(entity);
        todo.clearDomainEvents();
    }

    /**
     * Hibernate ignores a version set by hand on an already-loaded entity (it uses the loaded snapshot in the
     * UPDATE's WHERE clause), so a stale aggregate must be rejected here or the update would silently win.
     */
    private TodoPersistenceEntity requireSameVersion(TodoPersistenceEntity existing, Todo todo) {
        if (!Objects.equals(existing.getVersion(), todo.getVersion())) {
            throw new ObjectOptimisticLockingFailureException(TodoPersistenceEntity.class, existing.getId());
        }
        return existing;
    }
}
