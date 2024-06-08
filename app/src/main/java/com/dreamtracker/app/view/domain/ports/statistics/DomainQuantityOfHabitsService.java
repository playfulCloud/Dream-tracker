package com.dreamtracker.app.view.domain.ports.statistics;

import com.dreamtracker.app.habit.adapters.api.HabitTrackingRequest;
import com.dreamtracker.app.infrastructure.exception.EntityNotFoundException;
import com.dreamtracker.app.infrastructure.exception.ExceptionMessages;
import com.dreamtracker.app.view.adapters.api.QuantityOfHabitsComponentResponse;
import com.dreamtracker.app.view.adapters.api.StatsComponentResponse;
import com.dreamtracker.app.view.domain.model.aggregate.QuantityOfHabitsAggregate;
import com.dreamtracker.app.view.domain.ports.QuantityOfHabitsAggregateRepositoryPort;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DomainQuantityOfHabitsService implements StatsTemplate {

  private final QuantityOfHabitsAggregateRepositoryPort quantityOfHabitsAggregateRepositoryPort;

  @Override
  public StatsComponentResponse initializeAggregates(UUID habitId) {
    var quantityOfHabits = initialize(habitId);
    var quantityOfHabitsSaveToDB = quantityOfHabitsAggregateRepositoryPort.save(quantityOfHabits);
    return mapToResponse(quantityOfHabitsSaveToDB);
  }

  @Override
  public StatsComponentResponse updateAggregatesAndCalculateResponse(
      UUID habitId, HabitTrackingRequest habitTrackingRequest) {
    return null;
  }

  @Override
  public StatsComponentResponse getCalculateResponse(UUID habitId) {
    var quantityOfHabits = quantityOfHabitsAggregateRepositoryPort.findByHabitUUID(habitId).orElseThrow(()->new EntityNotFoundException(ExceptionMessages.entityNotFoundExceptionMessage));
    return mapToResponse(quantityOfHabits);
  }

  private StatsComponentResponse mapToResponse(
      QuantityOfHabitsAggregate quantityOfHabitsAggregate) {
    return QuantityOfHabitsComponentResponse.builder()
        .done(quantityOfHabitsAggregate.getDoneHabits())
        .undone(quantityOfHabitsAggregate.getUnDoneHabits())
        .trend(calculateTrend(quantityOfHabitsAggregate))
        .build();
  }

  private QuantityOfHabitsAggregate initialize(UUID habitUUID) {
    return QuantityOfHabitsAggregate.builder()
        .habitUUID(habitUUID)
        .doneHabits(0)
        .unDoneHabits(0)
        .unDoneInRow(0)
        .doneInRow(0)
        .build();
  }

  private enum TrendStatus {
    SLOW_RISING,
    SLOW_FALLING,
    POSITIVE_PLATEAU,
    NEGATIVE,
    CREATED,
  }

  private String calculateTrend(QuantityOfHabitsAggregate quantityOfHabitsAggregate) {
    return TrendStatus.CREATED.toString();
  }
}
