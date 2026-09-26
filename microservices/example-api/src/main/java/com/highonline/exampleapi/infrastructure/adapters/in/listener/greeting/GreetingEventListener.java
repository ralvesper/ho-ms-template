package com.highonline.exampleapi.infrastructure.adapters.in.listener.greeting;

import com.highonline.exampleapi.core.domain.model.greeting.GreetingCreatedEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

/** Adapter de entrada: reage a eventos de domínio; troque o log por uma chamada a uma porta in/out. */
@Component
@Slf4j
public class GreetingEventListener {

    @EventListener
    public void listen(GreetingCreatedEvent event) {
        log.info("Greeting {} created", event.greetingId());
    }
}
