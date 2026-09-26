package com.highonline.exampleapi.core.ports.in.greeting;

public interface ForTranslatingGreetings {
    TranslationOutput translate(String id, String lang);
}
