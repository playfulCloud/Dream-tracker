package com.dreamtracker.app.infrastructure.repository;

import com.dreamtracker.app.view.domain.model.aggregate.StreakAggregate;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface SpringDataStreakAggregateRepository extends JpaRepository<StreakAggregate, UUID> {
    List<StreakAggregate> findByHabitUUID(UUID habitUUID);
}
