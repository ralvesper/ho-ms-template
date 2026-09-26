package com.highonline.exampleapi.core.domain.model.todo;

import java.time.OffsetDateTime;

public record TodoCompletedEvent(TodoId todoId, OffsetDateTime completedAt) {}
