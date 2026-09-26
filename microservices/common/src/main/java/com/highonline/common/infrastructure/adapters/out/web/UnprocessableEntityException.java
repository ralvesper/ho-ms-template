package com.highonline.common.infrastructure.adapters.out.web;

public class UnprocessableEntityException extends RuntimeException {
    public UnprocessableEntityException() {
    }

    public UnprocessableEntityException(String message, Throwable cause) {
        super(message, cause);
    }

    public UnprocessableEntityException(String message) {
        super(message);
    }
}
