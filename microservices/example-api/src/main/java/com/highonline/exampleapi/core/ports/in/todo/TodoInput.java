package com.highonline.exampleapi.core.ports.in.todo;

import jakarta.validation.constraints.NotBlank;

public record TodoInput(@NotBlank String title) {}
