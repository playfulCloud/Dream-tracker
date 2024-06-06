package com.dreamtracker.app.view.domain.ports;

import com.dreamtracker.app.view.domain.model.BreaksAggregate;
import com.dreamtracker.app.view.domain.model.SingleDayAggregate;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface SingleDayAggregateRepositoryPort {
    SingleDayAggregate save(SingleDayAggregate singleDayAggregate);
    Boolean existsById(UUID id);
    void deleteById(UUID id);
    Optional<SingleDayAggregate> findById(UUID id);
    List<BreaksAggregate> findByHabitUUID(UUID habitUUID);
}
