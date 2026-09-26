package com.highonline.exampleapi.infrastructure.adapters.out.persistence.todo;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Version;
import lombok.*;
import org.springframework.data.domain.AbstractAggregateRoot;

import java.time.OffsetDateTime;
import java.util.Collection;
import java.util.UUID;

@Entity
@Table(name = "todo")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TodoPersistenceEntity extends AbstractAggregateRoot<TodoPersistenceEntity> {

    @Id
    private UUID id;
    private String title;
    private boolean completed;
    private OffsetDateTime createdAt;

    @Version
    private Long version;

    public void addEvents(Collection<Object> events) {
        events.forEach(this::registerEvent);
    }
}
