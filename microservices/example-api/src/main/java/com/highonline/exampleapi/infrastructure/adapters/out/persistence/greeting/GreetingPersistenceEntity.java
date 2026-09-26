package com.highonline.exampleapi.infrastructure.adapters.out.persistence.greeting;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Table(name = "greeting")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GreetingPersistenceEntity {

    @Id
    private UUID id;
    private String message;
    private OffsetDateTime createdAt;
}
