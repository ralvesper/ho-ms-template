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
}
