package com.dreamtracker.app.view.domain.ports;

import com.dreamtracker.app.view.domain.model.BreaksAggregate;
import com.dreamtracker.app.view.domain.model.SingleDayAggregate;
import com.dreamtracker.app.view.domain.model.StreakAggregate;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface StreakAggregateRepositoryPort {
    StreakAggregate save(StreakAggregate streakAggregate);
    Boolean existsById(UUID id);
    void deleteById(UUID id);
    Optional<StreakAggregate> findById(UUID id);
    List<StreakAggregate> findByHabitUUID(UUID habitUUID);
}
