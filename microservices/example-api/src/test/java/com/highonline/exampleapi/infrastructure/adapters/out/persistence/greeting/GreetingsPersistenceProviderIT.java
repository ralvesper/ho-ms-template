package com.highonline.exampleapi.infrastructure.adapters.out.persistence.greeting;

import com.highonline.exampleapi.core.domain.model.greeting.Greeting;
import com.highonline.exampleapi.core.domain.model.greeting.Greetings;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.postgresql.PostgreSQLContainer;

import java.time.temporal.ChronoUnit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.Assertions.within;

@SpringBootTest
@Testcontainers
class GreetingsPersistenceProviderIT {

    @Container
    @ServiceConnection
    static PostgreSQLContainer postgres = new PostgreSQLContainer("postgres:17-alpine");

    @Autowired
    Greetings greetings;

    @Test
    void addThenOfIdRoundTripsThroughAssemblerAndDisassembler() {
        Greeting greeting = Greeting.brandNew("oi");
        greetings.add(greeting);

        Greeting loaded = greetings.ofId(greeting.getId()).orElseThrow();

        assertThat(loaded.getId()).isEqualTo(greeting.getId());
        assertThat(loaded.getMessage()).isEqualTo("oi");
        assertThat(loaded.getCreatedAt()).isCloseTo(greeting.getCreatedAt(), within(1, ChronoUnit.MILLIS));
        assertThat(loaded.getVersion()).isZero();
        assertThat(loaded.domainEvents()).isEmpty();
        assertThat(greeting.domainEvents()).as("provider clears the aggregate's events after saving").isEmpty();
    }

    @Test
    void addOnExistingAggregateMergesIntoTheSameRowAndBumpsVersion() {
        Greeting greeting = Greeting.brandNew("oi");
        greetings.add(greeting);

        Greeting loaded = greetings.ofId(greeting.getId()).orElseThrow();
        loaded.changeMessage("olá");
        greetings.add(loaded);

        Greeting reloaded = greetings.ofId(greeting.getId()).orElseThrow();
        assertThat(reloaded.getMessage()).isEqualTo("olá");
        assertThat(reloaded.getVersion()).isEqualTo(1L);
    }

    @Test
    void staleUpdateIsRejectedByOptimisticLocking() {
        Greeting greeting = Greeting.brandNew("oi");
        greetings.add(greeting);

        Greeting first = greetings.ofId(greeting.getId()).orElseThrow();
        Greeting second = greetings.ofId(greeting.getId()).orElseThrow(); // same version as "first"

        first.changeMessage("from first");
        greetings.add(first);

        second.changeMessage("from second");
        assertThatThrownBy(() -> greetings.add(second)).isInstanceOf(ObjectOptimisticLockingFailureException.class);

        assertThat(greetings.ofId(greeting.getId()).orElseThrow().getMessage()).isEqualTo("from first");
    }
}
