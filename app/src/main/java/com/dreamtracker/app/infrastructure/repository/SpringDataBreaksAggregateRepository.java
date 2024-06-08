package com.dreamtracker.app.infrastructure.repository;

import com.dreamtracker.app.view.domain.model.aggregate.BreaksAggregate;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface SpringDataBreaksAggregateRepository extends JpaRepository<BreaksAggregate, UUID> {
    Optional<BreaksAggregate> findByHabitUUID(UUID habitUUID);
}
