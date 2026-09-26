package com.highonline.exampleapi.core.domain.model.greeting;

import com.highonline.exampleapi.core.domain.model.commons.Email;
import com.fasterxml.uuid.Generators;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.OffsetDateTime;
import java.util.UUID;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Greeting {

    private final GreetingId id;
    private final UUID traceId; // UUIDv7
    private final String message;
    private final Email recipientEmail;
    private final OffsetDateTime createdAt;

    public static Greeting brandNew(String message, Email recipientEmail) {
        return new Greeting(GreetingId.generate(), Generators.timeBasedEpochGenerator().generate(),
                message, recipientEmail, OffsetDateTime.now());
    }

    public static Greeting existing(GreetingId id, UUID traceId, String message, Email recipientEmail,
                                    OffsetDateTime createdAt) {
        return new Greeting(id, traceId, message, recipientEmail, createdAt);
    }
}
