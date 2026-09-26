package com.highonline.exampleapi.core.domain.model.todo;

import java.util.UUID;

public record TodoId(UUID value) {

    public static TodoId generate() {
        return new TodoId(UUID.randomUUID());
    }

    public static TodoId from(String raw) {
        try {
            return new TodoId(UUID.fromString(raw));
        } catch (IllegalArgumentException e) {
            throw new TodoNotFoundException(raw);
        }
    }

    @Override
    public String toString() {
        return value.toString();
    }
}
