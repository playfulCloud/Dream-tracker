package com.dreamtracker.app.infrastructure.repository;

import com.dreamtracker.app.view.domain.model.aggregate.SingleDayAggregate;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface SpringDataSingleDayAggregateRepository extends JpaRepository<SingleDayAggregate, UUID> {
    Optional<SingleDayAggregate> findByHabitUUID(UUID habitUUID);
}
