package com.dreamtracker.app.view.domain.ports.statistics;

import com.dreamtracker.app.habit.adapters.api.HabitTrackResponse;
import com.dreamtracker.app.infrastructure.exception.EntityNotFoundException;
import com.dreamtracker.app.infrastructure.exception.ExceptionMessages;
import com.dreamtracker.app.view.adapters.api.BreakComponentResponse;
import com.dreamtracker.app.view.adapters.api.StatsComponentResponse;
import com.dreamtracker.app.view.config.StatsAggregatorObserver;
import com.dreamtracker.app.view.domain.model.aggregate.BreaksAggregate;
import com.dreamtracker.app.view.domain.ports.BreaksAggregateRepositoryPort;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DomainBreaksService implements StatsAggregatorObserver {

  private final BreaksAggregateRepositoryPort breaksAggregateRepositoryPort;

  @Override
  public StatsComponentResponse initializeAggregate(UUID habitUUID) {
    var breakAggregate = BreaksAggregate.builder().habitUUID(habitUUID).build();
    var aggregateSavedToDb = breaksAggregateRepositoryPort.save(breakAggregate);
    return mapToResponse(aggregateSavedToDb);
  }

  @Override
  public StatsComponentResponse updateAggregate(
      UUID habitId, HabitTrackResponse habitTrackResponse) {
    BreaksAggregate aggregateFoundByHabitId =
        breaksAggregateRepositoryPort
            .findByHabitUUID(habitId)
            .orElseThrow(
                () ->
                    new EntityNotFoundException(ExceptionMessages.entityNotFoundExceptionMessage));

    String status = habitTrackResponse.status();
    var currentSumOfBreaks = aggregateFoundByHabitId.getSumOfBreaks();
    var currentBreakQuantity = aggregateFoundByHabitId.getBreaksQuantity();
    switch (status){
      case "DONE":
        if(aggregateFoundByHabitId.isBreak()){
          aggregateFoundByHabitId.setBreak(false);
        }
        break;
      case "UNDONE":
        if(!aggregateFoundByHabitId.isBreak()){
          aggregateFoundByHabitId.setBreaksQuantity(currentBreakQuantity+1);
        }
        aggregateFoundByHabitId.setSumOfBreaks(currentSumOfBreaks+1);
        break;
    }
    var updatedAggregateSavedToDB = breaksAggregateRepositoryPort.save(aggregateFoundByHabitId);
    return mapToResponse(updatedAggregateSavedToDB);
  }

  @Override
  public StatsComponentResponse getAggregate(UUID habitId) {
    var aggregateFoundByHabitId =
        breaksAggregateRepositoryPort
            .findByHabitUUID(habitId)
            .orElseThrow(() -> new EntityNotFoundException(ExceptionMessages.entityNotFoundExceptionMessage));
    return mapToResponse(aggregateFoundByHabitId);
  }

  private StatsComponentResponse mapToResponse(BreaksAggregate breaksAggregate) {
    return BreakComponentResponse.builder().averageBreak(calculateAverageBreak(breaksAggregate)).build();
  }

  private double calculateAverageBreak(BreaksAggregate breaksAggregate) {
    if(breaksAggregate.getBreaksQuantity() == 0){
      return 0;
    }
    return (double) breaksAggregate.getSumOfBreaks() /breaksAggregate.getBreaksQuantity();
  }

}
