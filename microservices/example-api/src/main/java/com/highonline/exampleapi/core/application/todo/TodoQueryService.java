package com.highonline.exampleapi.core.application.todo;

import com.highonline.exampleapi.core.domain.model.todo.TodoId;
import com.highonline.exampleapi.core.ports.in.todo.ForQueryingTodos;
import com.highonline.exampleapi.core.ports.in.todo.TodoOutput;
import com.highonline.exampleapi.core.ports.out.todo.ForObtainingTodos;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TodoQueryService implements ForQueryingTodos {

    private final ForObtainingTodos forObtainingTodos;

    @Override
    public TodoOutput findById(String id) {
        return forObtainingTodos.findById(TodoId.from(id));
    }

    @Override
    public Page<TodoOutput> findAll(Pageable pageable) {
        return forObtainingTodos.findAll(pageable);
    }
}
