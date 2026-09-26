package com.highonline.exampleapi.core.application.greeting;

import com.highonline.exampleapi.core.domain.model.greeting.GreetingId;
import com.highonline.exampleapi.core.ports.in.greeting.ForQueryingGreetings;
import com.highonline.exampleapi.core.ports.in.greeting.GreetingOutput;
import com.highonline.exampleapi.core.ports.out.greeting.ForObtainingGreetings;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GreetingQueryService implements ForQueryingGreetings {

    private final ForObtainingGreetings forObtainingGreetings;

    @Override
    public GreetingOutput findById(String id) {
        return forObtainingGreetings.findById(GreetingId.from(id));
    }

    @Override
    public Page<GreetingOutput> findAll(Pageable pageable) {
        return forObtainingGreetings.findAll(pageable);
    }
}
