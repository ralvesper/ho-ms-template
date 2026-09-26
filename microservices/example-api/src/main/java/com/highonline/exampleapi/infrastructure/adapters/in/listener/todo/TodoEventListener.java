package com.highonline.exampleapi.infrastructure.adapters.in.listener.todo;

import com.highonline.exampleapi.core.domain.model.todo.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

/** Adapter de entrada: reage a eventos de domínio; troque os logs por chamadas a portas in/out. */
@Component
@Slf4j
public class TodoEventListener {

    @EventListener
    public void listen(TodoCreatedEvent event) {
        log.info("Todo {} created", event.todoId());
    }

    @EventListener
    public void listen(TodoTitleChangedEvent event) {
        log.info("Todo {} title changed", event.todoId());
    }

    @EventListener
    public void listen(TodoCompletedEvent event) {
        log.info("Todo {} completed", event.todoId());
    }

    @EventListener
    public void listen(TodoReopenedEvent event) {
        log.info("Todo {} reopened", event.todoId());
    }

    @EventListener
    public void listen(TodoRemovedEvent event) {
        log.info("Todo {} removed", event.todoId());
    }
}
