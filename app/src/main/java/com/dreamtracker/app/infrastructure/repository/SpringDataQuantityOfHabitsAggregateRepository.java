package com.dreamtracker.app.infrastructure.repository;

import com.dreamtracker.app.view.domain.model.BreaksAggregate;
import com.dreamtracker.app.view.domain.model.DependingOnDayAggregate;
import com.dreamtracker.app.view.domain.model.QuantityOfHabitsAggregate;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface SpringDataQuantityOfHabitsAggregateRepository extends JpaRepository<QuantityOfHabitsAggregate, UUID> {
    List<QuantityOfHabitsAggregate> findByHabitUUID(UUID habitUUID);
}
