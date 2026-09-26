package com.highonline.exampleapi.infrastructure.adapters.out.persistence.greeting;

import org.springframework.data.jpa.repository.JpaRepository;

public interface GreetingPersistenceEntityRepository extends JpaRepository<GreetingPersistenceEntity, Long> {
}
