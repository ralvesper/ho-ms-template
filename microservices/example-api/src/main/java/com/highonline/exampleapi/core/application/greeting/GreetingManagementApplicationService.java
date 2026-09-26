package com.highonline.exampleapi.core.application.greeting;

import com.highonline.exampleapi.core.domain.model.greeting.Greeting;
import com.highonline.exampleapi.core.domain.model.greeting.GreetingId;
import com.highonline.exampleapi.core.domain.model.greeting.GreetingNotFoundException;
import com.highonline.exampleapi.core.domain.model.greeting.Greetings;
import com.highonline.exampleapi.core.ports.in.greeting.ForManagingGreetings;
import com.highonline.exampleapi.core.ports.in.greeting.GreetingInput;
import com.highonline.exampleapi.core.ports.in.greeting.GreetingUpdateInput;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class GreetingManagementApplicationService implements ForManagingGreetings {

    private final Greetings greetings;

    @Transactional
    @Override
    public String create(GreetingInput input) {
        Greeting greeting = Greeting.brandNew(input.message());
        greetings.add(greeting);
        return greeting.getId().toString();
    }

    @Transactional
    @Override
    public void changeMessage(String rawId, GreetingUpdateInput input) {
        Greeting greeting = greetings.ofId(GreetingId.from(rawId))
                .orElseThrow(() -> new GreetingNotFoundException(rawId));
        greeting.changeMessage(input.message());
        greetings.add(greeting);
    }
}
