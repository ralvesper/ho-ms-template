package com.highonline.exampleapi.core.domain.model.greeting;

import java.util.Optional;

/** Repositório do aggregate (lado de escrita). */
public interface Greetings {
    Optional<Greeting> ofId(GreetingId id);

    /** Insere ou atualiza o agregado. */
    void add(Greeting greeting);
}
