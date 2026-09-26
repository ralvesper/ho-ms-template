package com.highonline.exampleapi.infrastructure.adapters.out.persistence.greeting;

import com.highonline.exampleapi.core.domain.model.greeting.GreetingId;
import com.highonline.exampleapi.core.domain.model.greeting.GreetingNotFoundException;
import com.highonline.exampleapi.core.ports.in.greeting.GreetingOutput;
import com.highonline.exampleapi.core.ports.out.greeting.ForObtainingGreetings;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ForObtainingGreetingsJpaImpl implements ForObtainingGreetings {

    private final GreetingPersistenceEntityRepository repository;

    @Override
    public GreetingOutput findById(GreetingId id) {
        return repository.findById(id.value()).map(this::toOutput)
                .orElseThrow(() -> new GreetingNotFoundException(id.toString()));
    }

    @Override
    public Page<GreetingOutput> findAll(Pageable pageable) {
        return repository.findAll(pageable).map(this::toOutput);
    }

    private GreetingOutput toOutput(GreetingPersistenceEntity e) {
        return new GreetingOutput(e.getId().toString(), e.getMessage(), e.getCreatedAt());
    }
}
