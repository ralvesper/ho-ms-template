package com.highonline.exampleapi.core.domain.model.greeting;

import java.util.Optional;

/** Repositório do aggregate (lado de escrita). */
public interface Greetings {
    Optional<Greeting> ofId(GreetingId id);
    void add(Greeting greeting);
}
