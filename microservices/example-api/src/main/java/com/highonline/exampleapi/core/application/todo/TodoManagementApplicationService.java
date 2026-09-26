package com.highonline.exampleapi.core.application.todo;

import com.highonline.exampleapi.core.domain.model.todo.Todo;
import com.highonline.exampleapi.core.domain.model.todo.TodoId;
import com.highonline.exampleapi.core.domain.model.todo.TodoNotFoundException;
import com.highonline.exampleapi.core.domain.model.todo.Todos;
import com.highonline.exampleapi.core.ports.in.todo.ForManagingTodos;
import com.highonline.exampleapi.core.ports.in.todo.TodoInput;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.function.Consumer;

@Service
@RequiredArgsConstructor
public class TodoManagementApplicationService implements ForManagingTodos {

    private final Todos todos;

    @Transactional
    @Override
    public String create(TodoInput input) {
        Todo todo = Todo.brandNew(input.title());
        todos.add(todo);
        return todo.getId().toString();
    }

    @Transactional
    @Override
    public void changeTitle(String id, TodoInput input) {
        update(id, todo -> todo.changeTitle(input.title()));
    }

    @Transactional
    @Override
    public void complete(String id) {
        update(id, Todo::complete);
    }

    @Transactional
    @Override
    public void reopen(String id) {
        update(id, Todo::reopen);
    }

    @Transactional
    @Override
    public void remove(String id) {
        Todo todo = load(id);
        todo.markAsRemoved();
        todos.remove(todo);
    }

    private void update(String id, Consumer<Todo> change) {
        Todo todo = load(id);
        change.accept(todo);
        todos.add(todo);
    }

    private Todo load(String id) {
        return todos.ofId(TodoId.from(id)).orElseThrow(() -> new TodoNotFoundException(id));
    }
}
