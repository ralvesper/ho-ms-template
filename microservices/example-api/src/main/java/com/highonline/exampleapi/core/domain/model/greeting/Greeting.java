package com.highonline.exampleapi.core.domain.model.greeting;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.OffsetDateTime;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Greeting {

    private final GreetingId id;
    private final String message;
    private final OffsetDateTime createdAt;

    public static Greeting brandNew(String message) {
        return new Greeting(GreetingId.generate(), message, OffsetDateTime.now());
    }

    public static Greeting existing(GreetingId id, String message, OffsetDateTime createdAt) {
        return new Greeting(id, message, createdAt);
    }
}
