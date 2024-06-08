package com.dreamtracker.app.infrastructure.repository;

import com.dreamtracker.app.view.domain.model.aggregate.QuantityOfHabitsAggregate;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface SpringDataQuantityOfHabitsAggregateRepository extends JpaRepository<QuantityOfHabitsAggregate, UUID> {
    Optional<QuantityOfHabitsAggregate> findByHabitUUID(UUID habitUUID);
}
