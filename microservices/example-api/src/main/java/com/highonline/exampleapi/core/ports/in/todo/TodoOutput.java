package com.highonline.exampleapi.core.ports.in.todo;

import java.time.OffsetDateTime;

public record TodoOutput(String id, String title, boolean completed, OffsetDateTime createdAt) {}
