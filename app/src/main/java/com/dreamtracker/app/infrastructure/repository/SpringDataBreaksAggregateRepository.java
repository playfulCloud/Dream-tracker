package com.dreamtracker.app.infrastructure.repository;

import com.dreamtracker.app.view.domain.model.aggregate.BreaksAggregate;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface SpringDataBreaksAggregateRepository extends JpaRepository<BreaksAggregate, UUID> {
    List<BreaksAggregate> findByHabitUUID(UUID habitUUID);
}
