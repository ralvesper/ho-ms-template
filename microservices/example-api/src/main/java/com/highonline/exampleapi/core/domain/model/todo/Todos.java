package com.highonline.exampleapi.core.domain.model.todo;

import java.util.Optional;

/** Repositório do aggregate (lado de escrita). */
public interface Todos {
    Optional<Todo> ofId(TodoId id);

    /** Insere ou atualiza o agregado. */
    void add(Todo todo);

    void remove(Todo todo);
}
