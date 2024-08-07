package com.dreamtracker.app.view.domain.ports.statistics;

import com.dreamtracker.app.habit.adapters.api.HabitTrackResponse;
import com.dreamtracker.app.infrastructure.exception.EntityNotFoundException;
import com.dreamtracker.app.infrastructure.exception.ExceptionMessages;
import com.dreamtracker.app.view.adapters.api.StatsComponentResponse;
import com.dreamtracker.app.view.adapters.api.StreakComponentResponse;
import com.dreamtracker.app.view.config.StatsAggregatorObserver;
import com.dreamtracker.app.view.domain.model.aggregate.StreakAggregate;
import com.dreamtracker.app.view.domain.ports.StreakAggregateRepositoryPort;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DomainStreakService implements StatsAggregatorObserver {

  private final StreakAggregateRepositoryPort streakAggregateRepositoryPort;

  @Override
  public StatsComponentResponse initializeAggregate(UUID habitUUID) {
    var streakAggregate = initialize(habitUUID);
    var streakAggregateSaveToDB = streakAggregateRepositoryPort.save(streakAggregate);
    return mapToResponse(streakAggregateSaveToDB);
  }

  @Override
  public StatsComponentResponse updateAggregate(UUID habitUUID, HabitTrackResponse habitTrackResponse){
    var streakAggregate =
        streakAggregateRepositoryPort
            .findByHabitUUID(habitUUID)
            .orElseThrow(
                () ->
                    new EntityNotFoundException(ExceptionMessages.entityNotFoundExceptionMessage));

    String status = habitTrackResponse.status();

    if (status.equals("DONE")) {
      streakAggregate.increaseCurrentStreak();
    } else {
      streakAggregate.setCurrentStreak(0);
    }
    var streakAggregateSavedToDB = streakAggregateRepositoryPort.save(streakAggregate);
    return mapToResponse(streakAggregateSavedToDB);
  }

  @Override
  public StatsComponentResponse getAggregate(UUID habitId) {
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
