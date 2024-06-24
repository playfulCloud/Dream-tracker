package com.dreamtracker.app.view.domain.ports;

import com.dreamtracker.app.view.domain.model.aggregate.DependingOnDayAggregate;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface DependingOnDayRepositoryPort {

    DependingOnDayAggregate save(DependingOnDayAggregate dependingOnDayAggregate);
    Boolean existsById(UUID id);
    void deleteById(UUID id);
    Optional<DependingOnDayAggregate> findById(UUID id);
    Optional<DependingOnDayAggregate> findByHabitUUID(UUID habitUUID);
}
