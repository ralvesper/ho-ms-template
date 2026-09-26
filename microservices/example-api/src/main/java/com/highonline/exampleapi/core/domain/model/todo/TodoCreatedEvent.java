package com.highonline.exampleapi.core.domain.model.todo;

import java.time.OffsetDateTime;

public record TodoCreatedEvent(TodoId todoId, String title, OffsetDateTime createdAt) {}
