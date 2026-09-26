package com.highonline.exampleapi.infrastructure.adapters.in.listener.greeting;

import com.highonline.exampleapi.core.domain.model.greeting.GreetingCreatedEvent;
import com.highonline.exampleapi.core.domain.model.greeting.GreetingMessageChangedEvent;
import com.highonline.exampleapi.core.ports.in.greeting.ForManagingGreetings;
import com.highonline.exampleapi.core.ports.in.greeting.GreetingInput;
import com.highonline.exampleapi.core.ports.in.greeting.GreetingUpdateInput;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.postgresql.PostgreSQLContainer;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;

/** Drives the use cases through the application port and checks the real listener bean received each event. */
@SpringBootTest
@Testcontainers
class GreetingEventListenerIT {

    @Container
    @ServiceConnection
    static PostgreSQLContainer postgres = new PostgreSQLContainer("postgres:17-alpine");

    @Autowired
    ForManagingGreetings forManagingGreetings;

    @MockitoSpyBean
    GreetingEventListener listener;

    @Test
    void listenerReceivesCreatedAndMessageChangedEvents() {
        String id = forManagingGreetings.create(new GreetingInput("oi"));
        forManagingGreetings.changeMessage(id, new GreetingUpdateInput("olá"));

        var created = ArgumentCaptor.forClass(GreetingCreatedEvent.class);
        verify(listener).listen(created.capture());
        assertThat(created.getValue().greetingId()).hasToString(id);
        assertThat(created.getValue().message()).isEqualTo("oi");

        var changed = ArgumentCaptor.forClass(GreetingMessageChangedEvent.class);
        verify(listener).listen(changed.capture());
        assertThat(changed.getValue().previousMessage()).isEqualTo("oi");
        assertThat(changed.getValue().newMessage()).isEqualTo("olá");
    }
}
