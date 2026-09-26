package com.highonline.exampleapi.infrastructure.adapters.out.persistence.greeting;

import com.highonline.exampleapi.core.domain.model.commons.Email;
import com.highonline.exampleapi.core.domain.model.greeting.Greeting;
import com.highonline.exampleapi.core.domain.model.greeting.GreetingId;
import org.springframework.stereotype.Component;

/** Entity JPA -> domínio. */
@Component
public class GreetingPersistenceEntityDisassembler {

    public Greeting toDomain(GreetingPersistenceEntity e) {
        return Greeting.existing(new GreetingId(e.getId()), e.getTraceId(), e.getMessage(),
                new Email(e.getRecipientEmail()), e.getCreatedAt());
    }
}
