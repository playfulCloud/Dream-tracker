package com.dreamtracker.app.view.domain.ports;

import com.dreamtracker.app.view.domain.model.aggregate.BreaksAggregate;
import com.dreamtracker.app.view.domain.model.aggregate.SingleDayAggregate;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface SingleDayAggregateRepositoryPort {
    SingleDayAggregate save(SingleDayAggregate singleDayAggregate);
    Boolean existsById(UUID id);
    void deleteById(UUID id);
    Optional<SingleDayAggregate> findById(UUID id);
    List<SingleDayAggregate> findByHabitUUID(UUID habitUUID);
}
