package com.highonline.exampleapi.core.domain.model.greeting;

/** Repositório do aggregate (lado de escrita). */
public interface Greetings {
    void add(Greeting greeting);
}
