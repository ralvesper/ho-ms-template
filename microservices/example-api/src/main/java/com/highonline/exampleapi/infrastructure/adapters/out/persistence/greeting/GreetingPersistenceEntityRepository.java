package com.highonline.exampleapi.infrastructure.adapters.out.persistence.greeting;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface GreetingPersistenceEntityRepository extends JpaRepository<GreetingPersistenceEntity, UUID> {
}
