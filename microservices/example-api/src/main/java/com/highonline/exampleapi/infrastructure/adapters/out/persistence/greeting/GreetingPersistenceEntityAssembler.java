package com.highonline.exampleapi.infrastructure.adapters.out.persistence.greeting;

import com.highonline.exampleapi.core.domain.model.greeting.Greeting;
import org.springframework.stereotype.Component;

/** Domínio -> entity JPA. */
@Component
public class GreetingPersistenceEntityAssembler {

    public GreetingPersistenceEntity fromDomain(Greeting greeting) {
        return GreetingPersistenceEntity.builder()
                .id(greeting.getId().value())
                .traceId(greeting.getTraceId())
                .message(greeting.getMessage())
                .recipientEmail(greeting.getRecipientEmail().value())
                .createdAt(greeting.getCreatedAt())
                .build();
    }
}
