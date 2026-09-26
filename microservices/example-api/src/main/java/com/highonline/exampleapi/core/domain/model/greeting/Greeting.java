package com.highonline.exampleapi.core.domain.model.greeting;

import com.highonline.common.core.domain.model.AbstractEventSourceEntity;
import com.highonline.common.core.domain.model.DomainException;
import lombok.Getter;

import java.time.OffsetDateTime;

@Getter
public class Greeting extends AbstractEventSourceEntity {

    private final GreetingId id;
    private String message;
    private final OffsetDateTime createdAt;
    private final Long version; // controle otimista, gerenciado pela persistência

    private Greeting(GreetingId id, String message, OffsetDateTime createdAt, Long version) {
        this.id = id;
        this.message = message;
        this.createdAt = createdAt;
        this.version = version;
    }

    public static Greeting brandNew(String message) {
        Greeting greeting = new Greeting(GreetingId.generate(), message, OffsetDateTime.now(), null);
        greeting.publishDomainEvent(new GreetingCreatedEvent(greeting.id, message, greeting.createdAt));
        return greeting;
    }

    public static Greeting existing(GreetingId id, String message, OffsetDateTime createdAt, Long version) {
        return new Greeting(id, message, createdAt, version);
    }

    public void changeMessage(String newMessage) {
        if (newMessage == null || newMessage.isBlank()) {
            throw new DomainException("Greeting message must not be blank");
        }
        String previous = this.message;
        this.message = newMessage;
        publishDomainEvent(new GreetingMessageChangedEvent(id, previous, newMessage));
    }
}
