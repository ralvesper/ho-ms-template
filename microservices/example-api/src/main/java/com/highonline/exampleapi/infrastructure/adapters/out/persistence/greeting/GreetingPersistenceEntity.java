package com.highonline.exampleapi.infrastructure.adapters.out.persistence.greeting;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;
import org.springframework.data.domain.AbstractAggregateRoot;

import java.time.OffsetDateTime;
import java.util.Collection;
import java.util.UUID;

/** Estende AbstractAggregateRoot: no save(), o Spring Data publica os eventos registrados. */
@Entity
@Table(name = "greeting")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GreetingPersistenceEntity extends AbstractAggregateRoot<GreetingPersistenceEntity> {

    @Id
    private UUID id;
    private String message;
    private OffsetDateTime createdAt;

    public void addEvents(Collection<Object> events) {
        events.forEach(this::registerEvent);
    }
}
