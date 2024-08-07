package com.dreamtracker.app.infrastructure.repository;

import com.dreamtracker.app.view.domain.model.aggregate.DependingOnDayAggregate;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface SpringDataDependingOnDayAggregateRepository extends JpaRepository<DependingOnDayAggregate, UUID> {
    Optional<DependingOnDayAggregate> findByHabitUUID(UUID habitUUID);
}
