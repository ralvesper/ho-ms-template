package com.highonline.exampleapi.core.ports.in.greeting;

public interface ForManagingGreetings {
    /** @return id (TSID) do greeting criado */
    String create(GreetingInput input);
}
