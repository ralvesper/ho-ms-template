package com.highonline.exampleapi.core.domain.model.todo;

public record TodoTitleChangedEvent(TodoId todoId, String previousTitle, String newTitle) {}
