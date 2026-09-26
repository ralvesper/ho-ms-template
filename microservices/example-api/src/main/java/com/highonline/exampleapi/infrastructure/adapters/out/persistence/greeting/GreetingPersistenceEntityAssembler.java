package com.highonline.exampleapi.infrastructure.adapters.out.persistence.greeting;

import com.highonline.exampleapi.core.domain.model.greeting.Greeting;
import org.springframework.stereotype.Component;

/** Domínio -> entity JPA. */
@Component
public class GreetingPersistenceEntityAssembler {

    public GreetingPersistenceEntity fromDomain(Greeting greeting) {
        return merge(new GreetingPersistenceEntity(), greeting);
    }

    /** Copia o estado do domínio para uma entity (nova ou já carregada) e registra os eventos. */
    public GreetingPersistenceEntity merge(GreetingPersistenceEntity entity, Greeting greeting) {
        entity.setId(greeting.getId().value());
        entity.setMessage(greeting.getMessage());
        entity.setCreatedAt(greeting.getCreatedAt());
        entity.setVersion(greeting.getVersion());
        entity.addEvents(greeting.domainEvents());
        return entity;
    }
}
