package com.dreamtracker.app.view.domain.ports.statistics;

import com.dreamtracker.app.habit.adapters.api.HabitTrackingRequest;
import com.dreamtracker.app.view.adapters.api.DependingOnDayComponentResponse;
import com.dreamtracker.app.view.adapters.api.StatsComponentResponse;
import com.dreamtracker.app.view.domain.model.aggregate.DependingOnDayAggregate;
import com.dreamtracker.app.view.domain.ports.DependingOnDayRepositoryPort;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DomainDependingOnDayService implements StatsTemplate {

  private final DependingOnDayRepositoryPort dependingOnDayRepositoryPort;

  @Override
  public StatsComponentResponse initializeAggregates(UUID habitId) {
    var dependingOnDay = initialize(habitId);
    var dependingOnDayAggregateSavedToDB = dependingOnDayRepositoryPort.save(dependingOnDay);
    return mapToResponse(dependingOnDayAggregateSavedToDB);
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

  private StatsComponentResponse mapToResponse(DependingOnDayAggregate dependingOnDayAggregate) {
    return DependingOnDayComponentResponse.builder()
        .mondayRateSuccessRate(
            calculateDailyRatio(
                dependingOnDayAggregate.getMondayDoneCount(),
                dependingOnDayAggregate.getMondayUnDoneCount()))
        .tuesdayRateSuccessRate(
            calculateDailyRatio(
                dependingOnDayAggregate.getTuesdayDoneCount(),
                dependingOnDayAggregate.getTuesdayUnDoneCount()))
        .wednesdayRateSuccessRate(
            calculateDailyRatio(
                dependingOnDayAggregate.getWednesdayDoneCount(),
                dependingOnDayAggregate.getWednesdayUnDoneCount()))
        .thursdayRateSuccessRate(
            calculateDailyRatio(
                dependingOnDayAggregate.getThursdayDoneCount(),
                dependingOnDayAggregate.getThursdayUnDoneCount()))
        .fridayRateSuccessRate(
            calculateDailyRatio(
                dependingOnDayAggregate.getFridayDoneCount(),
                dependingOnDayAggregate.getFridayUnDoneCount()))
        .saturdayRateSuccessRate(
            calculateDailyRatio(
                dependingOnDayAggregate.getSaturdayDoneCount(),
                dependingOnDayAggregate.getSaturdayUnDoneCount()))
        .sundayRateSuccessRate(
            calculateDailyRatio(
                dependingOnDayAggregate.getSundayDoneCount(),
                dependingOnDayAggregate.getSundayUnDoneCount()))
        .build();
  }

  private DependingOnDayAggregate initialize(UUID habitUUID) {
    return DependingOnDayAggregate.builder()
        .habitUUID(habitUUID)
        .mondayDoneCount(0)
        .mondayUnDoneCount(0)
        .tuesdayDoneCount(0)
        .tuesdayUnDoneCount(0)
        .wednesdayDoneCount(0)
        .wednesdayUnDoneCount(0)
        .thursdayDoneCount(0)
        .thursdayUnDoneCount(0)
        .fridayDoneCount(0)
        .fridayUnDoneCount(0)
        .saturdayDoneCount(0)
        .saturdayUnDoneCount(0)
        .sundayDoneCount(0)
        .sundayUnDoneCount(0)
        .build();
  }

  private double calculateDailyRatio(double doneCount, double undoneCount) {
    var all = doneCount + undoneCount;
    return (doneCount / all) * 100;
  }
}
