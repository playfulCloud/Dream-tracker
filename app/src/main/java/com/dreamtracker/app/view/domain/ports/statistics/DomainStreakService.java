package com.dreamtracker.app.view.domain.ports.statistics;

import com.dreamtracker.app.habit.adapters.api.HabitTrackResponse;
import com.dreamtracker.app.habit.adapters.api.HabitTrackingRequest;
import com.dreamtracker.app.infrastructure.exception.EntityNotFoundException;
import com.dreamtracker.app.infrastructure.exception.ExceptionMessages;
import com.dreamtracker.app.view.adapters.api.StatsComponentResponse;
import com.dreamtracker.app.view.adapters.api.StreakComponentResponse;
import com.dreamtracker.app.view.domain.model.aggregate.StreakAggregate;
import com.dreamtracker.app.view.domain.ports.StreakAggregateRepositoryPort;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DomainStreakService implements StatsTemplate {

  private final StreakAggregateRepositoryPort streakAggregateRepositoryPort;

  @Override
  public StatsComponentResponse initializeAggregates(UUID habitId) {
    var streakAggregate = initialize(habitId);
    var streakAggregateSaveToDB = streakAggregateRepositoryPort.save(streakAggregate);
    return mapToResponse(streakAggregateSaveToDB);
  }

  @Override
  public StatsComponentResponse updateAggregatesAndCalculateResponse(
      UUID habitId,  HabitTrackResponse habitTrackResponse){
    return null;
  }

  @Override
  public StatsComponentResponse getCalculateResponse(UUID habitId) {
    var streakAggregate =
        streakAggregateRepositoryPort
            .findByHabitUUID(habitId)
            .orElseThrow(() -> new EntityNotFoundException(ExceptionMessages.entityNotFoundExceptionMessage));
    return mapToResponse(streakAggregate);
  }

  private StatsComponentResponse mapToResponse(StreakAggregate streakAggregate) {
    return StreakComponentResponse.builder()
        .actual(streakAggregate.getCurrentStreak())
        .longest(streakAggregate.getLongestStreak())
        .build();
  }

  private StreakAggregate initialize(UUID habitUUID) {
    return StreakAggregate.builder().habitUUID(habitUUID).currentStreak(0).longestStreak(0).build();
  }
}
