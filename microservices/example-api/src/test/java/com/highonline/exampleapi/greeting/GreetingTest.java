package com.highonline.exampleapi.greeting;

import com.highonline.common.DomainException;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class GreetingTest {

    @Test
    void generatesTsidAndUuidV7() {
        Greeting g = new Greeting("oi", "a@b.com");
        assertThat(g.getId()).isPositive();
        assertThat(g.getTraceId().version()).isEqualTo(7);
    }

    @Test
    void rejectsInvalidEmail() {
        assertThatThrownBy(() -> new Greeting("oi", "not-an-email")).isInstanceOf(DomainException.class);
    }
}
