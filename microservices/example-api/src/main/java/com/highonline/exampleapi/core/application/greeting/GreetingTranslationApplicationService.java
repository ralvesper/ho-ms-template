package com.highonline.exampleapi.core.application.greeting;

import com.highonline.exampleapi.core.domain.model.greeting.GreetingId;
import com.highonline.exampleapi.core.domain.model.greeting.GreetingNotFoundException;
import com.highonline.exampleapi.core.domain.model.greeting.Greetings;
import com.highonline.exampleapi.core.ports.in.greeting.ForTranslatingGreetings;
import com.highonline.exampleapi.core.ports.in.greeting.TranslationOutput;
import com.highonline.exampleapi.core.ports.out.greeting.ForTranslatingText;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GreetingTranslationApplicationService implements ForTranslatingGreetings {

    private final Greetings greetings;
    private final ForTranslatingText forTranslatingText;

    @Override
    public TranslationOutput translate(String id, String lang) {
        var greeting = greetings.ofId(GreetingId.from(id)).orElseThrow(() -> new GreetingNotFoundException(id));
        return new TranslationOutput(id, lang, forTranslatingText.translate(greeting.getMessage(), lang));
    }
}
