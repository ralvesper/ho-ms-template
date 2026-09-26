package com.highonline.exampleapi.infrastructure.adapters.out.persistence.greeting;

import com.highonline.exampleapi.core.domain.model.greeting.Greeting;
import com.highonline.exampleapi.core.domain.model.greeting.GreetingId;
import com.highonline.exampleapi.core.domain.model.greeting.Greetings;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class GreetingsPersistenceProvider implements Greetings {

    private final GreetingPersistenceEntityRepository repository;
    private final GreetingPersistenceEntityAssembler assembler;
    private final GreetingPersistenceEntityDisassembler disassembler;

    @Transactional(readOnly = true)
    @Override
    public Optional<Greeting> ofId(GreetingId id) {
        return repository.findById(id.value()).map(disassembler::toDomainEntity);
    }

    @Transactional
    @Override
    public void add(Greeting greeting) {
        GreetingPersistenceEntity entity = repository.findById(greeting.getId().value())
                .map(existing -> assembler.merge(existing, greeting))
                .orElseGet(() -> assembler.fromDomain(greeting));
        repository.save(entity);
        greeting.clearDomainEvents();
    }
}
