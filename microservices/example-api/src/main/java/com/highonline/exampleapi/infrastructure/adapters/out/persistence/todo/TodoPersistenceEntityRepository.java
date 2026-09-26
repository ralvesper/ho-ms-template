package com.highonline.exampleapi.infrastructure.adapters.out.persistence.todo;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface TodoPersistenceEntityRepository extends JpaRepository<TodoPersistenceEntity, UUID> {
}
