package com.highonline.exampleapi.core.ports.in.greeting;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ForQueryingGreetings {
    GreetingOutput findById(String id);
    Page<GreetingOutput> findAll(Pageable pageable);
}
