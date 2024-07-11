package com.dreamtracker.app.view.domain.ports.statistics;

import com.dreamtracker.app.habit.adapters.api.HabitTrackResponse;
import com.dreamtracker.app.infrastructure.exception.EntityNotFoundException;
import com.dreamtracker.app.infrastructure.exception.ExceptionMessages;
import com.dreamtracker.app.infrastructure.utils.DateService;
import com.dreamtracker.app.view.adapters.api.SingleDayComponentResponse;
import com.dreamtracker.app.view.adapters.api.StatsComponentResponse;
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
public class DomainSingleDayService implements StatsTemplate {

  private final SingleDayAggregateRepositoryPort singleDayAggregateRepositoryPort;
  private final DateService dateService;

  @Override
  public StatsComponentResponse initializeAggregates(UUID habitId) {
    var singleDayAggregate = initialize(habitId);
    var singleDayAggregateSavedToSB = singleDayAggregateRepositoryPort.save(singleDayAggregate);
    return mapToResponse(singleDayAggregateSavedToSB);
  }

  @Override
  public StatsComponentResponse updateAggregatesAndCalculateResponse(
          UUID habitId, HabitTrackResponse habitTrackResponse) {
    var singleDayAggregate =
            singleDayAggregateRepositoryPort
                    .findByHabitUUID(habitId)
                    .orElseThrow(
                            () -> new EntityNotFoundException(ExceptionMessages.entityNotFoundExceptionMessage));
    DateTimeFormatter formatter = DateTimeFormatter.ISO_ZONED_DATE_TIME;

    String status = habitTrackResponse.status();
    String dateOfHabitTrack = habitTrackResponse.date();
    String aggregateDate = singleDayAggregate.getDate();

    LocalDate parsedAggregateDate = ZonedDateTime.parse(aggregateDate, formatter).toLocalDate();
    LocalDate habitTrackDate = ZonedDateTime.parse(dateOfHabitTrack, formatter).toLocalDate();
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
  public StatsComponentResponse getCalculateResponse(UUID habitId) {
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
