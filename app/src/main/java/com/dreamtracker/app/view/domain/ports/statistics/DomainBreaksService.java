package com.dreamtracker.app.view.domain.ports.statistics;

import com.dreamtracker.app.habit.adapters.api.HabitTrackingRequest;
import com.dreamtracker.app.view.adapters.api.BreakComponentResponse;
import com.dreamtracker.app.view.adapters.api.StatsComponentResponse;
import com.dreamtracker.app.view.domain.model.aggregate.BreaksAggregate;
import com.dreamtracker.app.view.domain.ports.BreaksAggregateRepositoryPort;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;



//TODO: redo
@Service
@RequiredArgsConstructor
public class DomainBreaksService implements StatsTemplate {

  private final BreaksAggregateRepositoryPort breaksAggregateRepositoryPort;

  @Override
  public StatsComponentResponse initializeAggregates(UUID habitId) {
    var breakAggregate = BreaksAggregate.builder().habitUUID(habitId).build();
    var aggregateSavedToDb = breaksAggregateRepositoryPort.save(breakAggregate);
    return mapToResponse(aggregateSavedToDb);
  }

  @Override
  public StatsComponentResponse updateAggregatesAndCalculateResponse(
      UUID habitId, HabitTrackingRequest habitTrackingRequest) {
    return null;
  }

  @Override
  public StatsComponentResponse getCalculateResponse(UUID habitId) {
    return null;
  }

  private StatsComponentResponse mapToResponse(BreaksAggregate breaksAggregate) {
    return BreakComponentResponse.builder()
        .averageBreak(calculateAverageBreak(breaksAggregate))
        .build();
  }

  // TODO: Need to rethink convention for this.
  private double calculateAverageBreak(BreaksAggregate breaksAggregate) {
    return 0.0;
  }
}
