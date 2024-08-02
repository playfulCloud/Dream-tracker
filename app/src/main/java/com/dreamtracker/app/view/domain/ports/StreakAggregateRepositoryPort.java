package com.dreamtracker.app.view.domain.ports;

import com.dreamtracker.app.view.domain.model.aggregate.StreakAggregate;

import java.util.Optional;
import java.util.UUID;

public interface StreakAggregateRepositoryPort {
    StreakAggregate save(StreakAggregate streakAggregate);
    Boolean existsById(UUID id);
    void deleteById(UUID id);
    Optional<StreakAggregate> findById(UUID id);
    Optional<StreakAggregate> findByHabitUUID(UUID habitUUID);
}
