package com.highonline.exampleapi.core.domain.model.greeting;

import com.highonline.common.core.domain.model.DomainEntityNotFoundException;

public class GreetingNotFoundException extends DomainEntityNotFoundException {

    public GreetingNotFoundException(String id) {
        super("Greeting " + id + " not found");
    }
}
