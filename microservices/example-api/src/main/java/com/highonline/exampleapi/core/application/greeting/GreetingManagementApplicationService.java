package com.highonline.exampleapi.core.application.greeting;

import com.highonline.exampleapi.core.domain.model.commons.Email;
import com.highonline.exampleapi.core.domain.model.greeting.Greeting;
import com.highonline.exampleapi.core.domain.model.greeting.Greetings;
import com.highonline.exampleapi.core.ports.in.greeting.ForManagingGreetings;
import com.highonline.exampleapi.core.ports.in.greeting.GreetingInput;
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
        Greeting greeting = Greeting.brandNew(input.message(), new Email(input.recipientEmail()));
        greetings.add(greeting);
        return greeting.getId().toString();
    }
}
