package com.dreamtracker.app.view.domain.ports.statistics;

import com.dreamtracker.app.habit.adapters.api.HabitTrackResponse;
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
      UUID habitId, HabitTrackResponse habitTrackResponse) {
    var quantityOfHabits =
        quantityOfHabitsAggregateRepositoryPort
            .findByHabitUUID(habitId)
            .orElseThrow(
                () ->
                    new EntityNotFoundException(ExceptionMessages.entityNotFoundExceptionMessage));
    String status = habitTrackResponse.status();

    switch (status) {
      case "DONE" -> quantityOfHabits.increaseDoneHabitsCount();
      case "UNDONE" -> quantityOfHabits.increaseUnDoneHabitsCount();
    }
    var quantityOfHabitsSavedToDB = quantityOfHabitsAggregateRepositoryPort.save(quantityOfHabits);
    return mapToResponse(quantityOfHabitsSavedToDB);
  }

  @Override
  public StatsComponentResponse getCalculateResponse(UUID habitId) {
    var quantityOfHabits = quantityOfHabitsAggregateRepositoryPort.findByHabitUUID(habitId).orElseThrow(()->new EntityNotFoundException(ExceptionMessages.entityNotFoundExceptionMessage));
    return mapToResponse(quantityOfHabits);
  }

  private StatsComponentResponse mapToResponse(
      QuantityOfHabitsAggregate quantityOfHabitsAggregate) {
            var showme =QuantityOfHabitsComponentResponse.builder()
        .done(quantityOfHabitsAggregate.getDoneHabits())
        .undone(quantityOfHabitsAggregate.getUnDoneHabits())
        .trend(calculateTrend(quantityOfHabitsAggregate))
        .build();
    return showme;
  }

  private QuantityOfHabitsAggregate initialize(UUID habitUUID) {
    return QuantityOfHabitsAggregate.builder()
        .habitUUID(habitUUID)
        .doneHabits(0)
        .unDoneHabits(0)
        .currentTrend(0)
        .build();
  }

  public enum TrendStatus {
    SLOW_RISING,
    SLOW_FALLING,
    RISING,
    FALLING,
    FAST_FALLING,
    FAST_RISING,
    POSITIVE_PLATEAU,
    NEGATIVE_PLATEAU,
    STAGNATION,
  }

  private String calculateTrend(QuantityOfHabitsAggregate quantityOfHabitsAggregate) {
    String calculatedTrend = "";
    var trendValue = quantityOfHabitsAggregate.getCurrentTrend();

    if (trendValue >= 7) {
      calculatedTrend = TrendStatus.POSITIVE_PLATEAU.toString();
    } else if (trendValue >= 5) {
      calculatedTrend = TrendStatus.FAST_RISING.toString();
    } else if (trendValue >= 3) {
      calculatedTrend = TrendStatus.RISING.toString();
    } else if (trendValue >= 1) {
      calculatedTrend = TrendStatus.SLOW_RISING.toString();
    } else if (trendValue <= -7) {
      calculatedTrend = TrendStatus.NEGATIVE_PLATEAU.toString();
    } else if (trendValue <= -5) {
      calculatedTrend = TrendStatus.FAST_FALLING.toString();
    } else if (trendValue <= -3) {
      calculatedTrend = TrendStatus.FALLING.toString();
    } else if (trendValue == -1) {
      calculatedTrend = TrendStatus.SLOW_FALLING.toString();
    } else if (trendValue == 0) {
      calculatedTrend = TrendStatus.STAGNATION.toString();
    }

    return calculatedTrend;
  }
}
