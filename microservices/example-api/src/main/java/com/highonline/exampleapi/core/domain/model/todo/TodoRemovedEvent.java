package com.highonline.exampleapi.core.domain.model.todo;

public record TodoRemovedEvent(TodoId todoId, String title) {}
