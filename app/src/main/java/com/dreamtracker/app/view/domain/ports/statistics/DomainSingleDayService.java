package com.dreamtracker.app.view.domain.ports.statistics;

import com.dreamtracker.app.habit.adapters.api.HabitTrackResponse;
import com.dreamtracker.app.infrastructure.exception.EntityNotFoundException;
import com.dreamtracker.app.infrastructure.exception.ExceptionMessages;
import com.dreamtracker.app.infrastructure.utils.DateService;
import com.dreamtracker.app.view.adapters.api.SingleDayComponentResponse;
import com.dreamtracker.app.view.adapters.api.StatsComponentResponse;
import com.dreamtracker.app.view.config.StatsAggregatorObserver;
import com.dreamtracker.app.view.domain.model.aggregate.SingleDayAggregate;
import com.dreamtracker.app.view.domain.ports.SingleDayAggregateRepositoryPort;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DomainSingleDayService implements StatsAggregatorObserver {

  private final SingleDayAggregateRepositoryPort singleDayAggregateRepositoryPort;
  private final DateService dateService;

  @Override
  public StatsComponentResponse initializeAggregate(UUID habitUUID) {
    var singleDayAggregate = initialize(habitUUID);
    var singleDayAggregateSavedToSB = singleDayAggregateRepositoryPort.save(singleDayAggregate);
    return mapToResponse(singleDayAggregateSavedToSB);
  }

  @Override
  public StatsComponentResponse updateAggregate(
          UUID habitUUID, HabitTrackResponse habitTrackResponse) {
    var singleDayAggregate =
            singleDayAggregateRepositoryPort
                    .findByHabitUUID(habitUUID)
                    .orElseThrow(
                            () -> new EntityNotFoundException(ExceptionMessages.entityNotFoundExceptionMessage));

    String status = habitTrackResponse.status();
    var dateOfHabitTrack = habitTrackResponse.date();
    var aggregateDate = singleDayAggregate.getDate();

    LocalDate parsedAggregateDate = aggregateDate.toLocalDate();
    LocalDate habitTrackDate = dateOfHabitTrack.toLocalDate();
    boolean areDatesTheSame = parsedAggregateDate.equals(habitTrackDate);

    if (status.equals("DONE")) {
      if (!areDatesTheSame) {
        singleDayAggregate.setActualCount(0);
        singleDayAggregate.setDate(dateOfHabitTrack);
      }
      singleDayAggregate.increaseActualCount();
    }
    var singleDayAggregateSaveToDB = singleDayAggregateRepositoryPort.save(singleDayAggregate);
    return mapToResponse(singleDayAggregateSaveToDB);
  }

  @Override
  public StatsComponentResponse getAggregate(UUID habitId) {
    var singleDayAggregate = singleDayAggregateRepositoryPort.findByHabitUUID(habitId).orElseThrow(() -> new EntityNotFoundException(ExceptionMessages.entityNotFoundExceptionMessage));
    return mapToResponse(singleDayAggregate);
  }

  private StatsComponentResponse mapToResponse(SingleDayAggregate singleDayAggregate) {
    return SingleDayComponentResponse.builder()
        .most(singleDayAggregate.getMostDone())
        .actual(singleDayAggregate.getActualCount())
        .date(singleDayAggregate.getDate())
        .build();
  }

  private SingleDayAggregate initialize(UUID habitUUID) {
    return SingleDayAggregate.builder()
        .habitUUID(habitUUID)
        .actualCount(0)
        .mostDone(0)
        .date(dateService.getCurrentDateInISO8601())
        .build();
  }

}
