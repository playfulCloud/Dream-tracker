package com.dreamtracker.app.view.domain.ports;

import com.dreamtracker.app.view.domain.model.BreaksAggregate;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface BreaksAggregateRepositoryPort {

    BreaksAggregate save(BreaksAggregate breaksAggregate) ;
    Boolean existsById(UUID id);
    void deleteById(UUID id);
    Optional<BreaksAggregate> findById(UUID id);
    List<BreaksAggregate> findByHabitUUID(UUID habitUUID);
}
