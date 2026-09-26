package com.highonline.exampleapi.core.ports.out.greeting;

import com.highonline.exampleapi.core.domain.model.greeting.GreetingId;
import com.highonline.exampleapi.core.ports.in.greeting.GreetingOutput;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ForObtainingGreetings {
    GreetingOutput findById(GreetingId id);
    Page<GreetingOutput> findAll(Pageable pageable);
}
