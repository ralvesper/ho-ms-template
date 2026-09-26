package com.highonline.exampleapi.core.domain.model.greeting;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class GreetingTest {

    @Test
    void idRoundTripsThroughString() {
        Greeting g = Greeting.brandNew("oi");
        assertThat(GreetingId.from(g.getId().toString())).isEqualTo(g.getId());
    }

    @Test
    void malformedIdIsNotFound() {
        assertThatThrownBy(() -> GreetingId.from("!!")).isInstanceOf(GreetingNotFoundException.class);
    }

    @Test
    void brandNewPublishesCreatedEventAndExistingDoesNot() {
        Greeting g = Greeting.brandNew("oi");
        assertThat(g.domainEvents()).singleElement().isInstanceOf(GreetingCreatedEvent.class);
        g.clearDomainEvents();
        assertThat(g.domainEvents()).isEmpty();
        assertThat(Greeting.existing(g.getId(), "oi", g.getCreatedAt(), 0L).domainEvents()).isEmpty();
    }

    @Test
    void changeMessagePublishesEventWithPreviousAndNewMessage() {
        Greeting g = Greeting.existing(GreetingId.generate(), "oi", java.time.OffsetDateTime.now(), 0L);
        g.changeMessage("olá");
        assertThat(g.getMessage()).isEqualTo("olá");
        assertThat(g.domainEvents()).singleElement().isEqualTo(
                new GreetingMessageChangedEvent(g.getId(), "oi", "olá"));
    }

    @Test
    void changeMessageRejectsBlank() {
        Greeting g = Greeting.brandNew("oi");
        assertThatThrownBy(() -> g.changeMessage(" "))
                .isInstanceOf(com.highonline.common.core.domain.model.DomainException.class);
    }
}
