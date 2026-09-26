package com.highonline.exampleapi.core.ports.in.greeting;

import java.time.OffsetDateTime;

public record GreetingOutput(String id, String message, OffsetDateTime createdAt) {}
