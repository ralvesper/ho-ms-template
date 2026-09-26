package com.highonline.exampleapi.core.domain.model.greeting;

import java.util.UUID;

public record GreetingId(UUID value) {

    public static GreetingId generate() {
        return new GreetingId(UUID.randomUUID());
    }

    public static GreetingId from(String raw) {
        try {
            return new GreetingId(UUID.fromString(raw));
        } catch (IllegalArgumentException e) {
            throw new GreetingNotFoundException(raw);
        }
    }

    @Override
    public String toString() {
        return value.toString();
    }
}
