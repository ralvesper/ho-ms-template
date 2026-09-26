package com.highonline.exampleapi.core.ports.in.todo;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ForQueryingTodos {
    TodoOutput findById(String id);
    Page<TodoOutput> findAll(Pageable pageable);
}
