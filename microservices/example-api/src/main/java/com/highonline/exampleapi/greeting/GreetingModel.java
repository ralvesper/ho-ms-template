package com.highonline.exampleapi.greeting;

import io.hypersistence.tsid.TSID;

import java.time.OffsetDateTime;
import java.util.UUID;

public record GreetingModel(String id, UUID traceId, String message, String recipientEmail, OffsetDateTime createdAt) {

    public static GreetingModel of(Greeting g) {
        return new GreetingModel(TSID.from(g.getId()).toString(), g.getTraceId(), g.getMessage(),
                g.getRecipientEmail(), g.getCreatedAt());
    }
}
