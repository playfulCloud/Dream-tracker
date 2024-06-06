package com.dreamtracker.app.view.domain.ports;

import com.dreamtracker.app.view.domain.model.BreaksAggregate;
import com.dreamtracker.app.view.domain.model.QuantityOfHabitsAggregate;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface QuantityOfHabitsAggregateRepositoryPort {
    QuantityOfHabitsAggregate save(QuantityOfHabitsAggregate quantityOfHabitsAggregate);
    Boolean existsById(UUID id);
    void deleteById(UUID id);
    Optional<QuantityOfHabitsAggregate> findById(UUID id);
    List<BreaksAggregate> findByHabitUUID(UUID habitUUID);
}
