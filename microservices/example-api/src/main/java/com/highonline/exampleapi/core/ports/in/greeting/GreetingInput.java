package com.highonline.exampleapi.core.ports.in.greeting;

import jakarta.validation.constraints.NotBlank;

public record GreetingInput(@NotBlank String message) {}
