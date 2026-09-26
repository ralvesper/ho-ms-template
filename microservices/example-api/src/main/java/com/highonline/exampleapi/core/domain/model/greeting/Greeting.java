package com.highonline.exampleapi.core.domain.model.greeting;

import com.highonline.common.core.domain.model.AbstractEventSourceEntity;
import lombok.Getter;

import java.time.OffsetDateTime;

@Getter
public class Greeting extends AbstractEventSourceEntity {

    private final GreetingId id;
    private final String message;
    private final OffsetDateTime createdAt;

    private Greeting(GreetingId id, String message, OffsetDateTime createdAt) {
        this.id = id;
        this.message = message;
        this.createdAt = createdAt;
    }

    public static Greeting brandNew(String message) {
        Greeting greeting = new Greeting(GreetingId.generate(), message, OffsetDateTime.now());
        greeting.publishDomainEvent(new GreetingCreatedEvent(greeting.id, message, greeting.createdAt));
        return greeting;
    }

    public static Greeting existing(GreetingId id, String message, OffsetDateTime createdAt) {
        return new Greeting(id, message, createdAt);
    }
}
