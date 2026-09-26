package com.highonline.exampleapi.core.domain.model.greeting;

public record GreetingMessageChangedEvent(GreetingId greetingId, String previousMessage, String newMessage) {}
