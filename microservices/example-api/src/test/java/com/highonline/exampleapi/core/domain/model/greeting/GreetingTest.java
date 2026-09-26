package com.highonline.exampleapi.core.domain.model.greeting;

import com.highonline.common.core.domain.model.DomainException;
import com.highonline.exampleapi.core.domain.model.commons.Email;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class GreetingTest {

    @Test
    void generatesTsidAndUuidV7() {
        Greeting g = Greeting.brandNew("oi", new Email("a@b.com"));
        assertThat(g.getId().value()).isPositive();
        assertThat(GreetingId.from(g.getId().toString())).isEqualTo(g.getId());
        assertThat(g.getTraceId().version()).isEqualTo(7);
    }

    @Test
    void rejectsInvalidEmail() {
        assertThatThrownBy(() -> new Email("not-an-email")).isInstanceOf(DomainException.class);
    }

    @Test
    void malformedIdIsNotFound() {
        assertThatThrownBy(() -> GreetingId.from("!!")).isInstanceOf(GreetingNotFoundException.class);
    }
}
