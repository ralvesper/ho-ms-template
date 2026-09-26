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
@Transactional(readOnly = true)
public class GreetingsPersistenceProvider implements Greetings {

    private final GreetingPersistenceEntityRepository repository;
    private final GreetingPersistenceEntityAssembler assembler;
    private final GreetingPersistenceEntityDisassembler disassembler;

    @Override
    public Optional<Greeting> ofId(GreetingId id) {
        return repository.findById(id.value()).map(disassembler::toDomain);
    }

    @Transactional
    @Override
    public void add(Greeting greeting) {
        repository.save(assembler.fromDomain(greeting));
    }
}
