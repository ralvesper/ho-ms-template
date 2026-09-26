package com.highonline.exampleapi.core.domain.model.greeting;

import java.time.OffsetDateTime;

public record GreetingCreatedEvent(GreetingId greetingId, String message, OffsetDateTime createdAt) {}
