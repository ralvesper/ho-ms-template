package com.highonline.exampleapi.core.ports.in.greeting;

public interface ForManagingGreetings {
    /** @return id do greeting criado */
    String create(GreetingInput input);

    void changeMessage(String id, GreetingUpdateInput input);
}
