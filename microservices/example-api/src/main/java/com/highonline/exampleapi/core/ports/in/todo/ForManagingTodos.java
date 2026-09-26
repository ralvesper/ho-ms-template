package com.highonline.exampleapi.core.ports.in.todo;

public interface ForManagingTodos {
    /** @return id do todo criado */
    String create(TodoInput input);

    void changeTitle(String id, TodoInput input);

    void complete(String id);

    void reopen(String id);

    void remove(String id);
}
