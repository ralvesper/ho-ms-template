package com.highonline.exampleapi.core.ports.in.greeting;

import java.time.OffsetDateTime;
import java.util.UUID;

public record GreetingOutput(String id, UUID traceId, String message, String recipientEmail, OffsetDateTime createdAt) {}
