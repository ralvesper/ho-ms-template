package com.highonline.exampleapi.core.domain.model.commons;

import com.highonline.common.core.domain.model.DomainException;
import org.apache.commons.validator.routines.EmailValidator;

public record Email(String value) {

    public Email {
        if (!EmailValidator.getInstance().isValid(value)) {
            throw new DomainException("Invalid e-mail: " + value);
        }
    }

    @Override
    public String toString() {
        return value;
    }
}
