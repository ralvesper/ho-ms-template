package com.highonline.exampleapi.core.domain.model.todo;

import com.highonline.common.core.domain.model.DomainEntityNotFoundException;

public class TodoNotFoundException extends DomainEntityNotFoundException {

    public TodoNotFoundException(String id) {
        super("Todo " + id + " not found");
    }
}
