package com.highonline.exampleapi.infrastructure.adapters.out.persistence.greeting;

import com.highonline.exampleapi.core.domain.model.greeting.Greeting;
import com.highonline.exampleapi.core.domain.model.greeting.GreetingId;
import org.springframework.stereotype.Component;

/** Entity JPA -> domínio. */
@Component
public class GreetingPersistenceEntityDisassembler {

    public Greeting toDomainEntity(GreetingPersistenceEntity entity) {
        return Greeting.existing(new GreetingId(entity.getId()), entity.getMessage(),
                entity.getCreatedAt(), entity.getVersion());
    }
}
