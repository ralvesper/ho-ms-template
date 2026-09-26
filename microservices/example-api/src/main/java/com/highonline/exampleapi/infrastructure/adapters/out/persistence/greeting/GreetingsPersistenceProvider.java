package com.highonline.exampleapi.infrastructure.adapters.out.persistence.greeting;

import com.highonline.exampleapi.core.domain.model.greeting.Greeting;
import com.highonline.exampleapi.core.domain.model.greeting.Greetings;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/** Domínio -> entity JPA. Em aggregates maiores, extraia Assembler/Disassembler. */
@Component
@RequiredArgsConstructor
public class GreetingsPersistenceProvider implements Greetings {

    private final GreetingPersistenceEntityRepository repository;

    @Transactional
    @Override
    public void add(Greeting greeting) {
        repository.save(GreetingPersistenceEntity.builder()
                .id(greeting.getId().value())
                .message(greeting.getMessage())
                .createdAt(greeting.getCreatedAt())
                .build());
    }
}
