package com.dreamtracker.app.view.domain.ports;

import com.dreamtracker.app.view.domain.model.aggregate.BreaksAggregate;
import com.dreamtracker.app.view.domain.model.aggregate.QuantityOfHabitsAggregate;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface QuantityOfHabitsAggregateRepositoryPort {
    QuantityOfHabitsAggregate save(QuantityOfHabitsAggregate quantityOfHabitsAggregate);
    Boolean existsById(UUID id);
    void deleteById(UUID id);
    Optional<QuantityOfHabitsAggregate> findById(UUID id);
    Optional<QuantityOfHabitsAggregate> findByHabitUUID(UUID habitUUID);
}
