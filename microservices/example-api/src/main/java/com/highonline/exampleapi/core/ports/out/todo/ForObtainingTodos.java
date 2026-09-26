package com.highonline.exampleapi.core.ports.out.todo;

import com.highonline.exampleapi.core.domain.model.todo.TodoId;
import com.highonline.exampleapi.core.ports.in.todo.TodoOutput;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ForObtainingTodos {
    TodoOutput findById(TodoId id);
    Page<TodoOutput> findAll(Pageable pageable);
}
