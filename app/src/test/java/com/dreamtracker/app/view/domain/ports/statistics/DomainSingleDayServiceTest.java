package com.dreamtracker.app.view.domain.ports.statistics;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.Mockito.when;

import com.dreamtracker.app.fixtures.HabitFixture;
import com.dreamtracker.app.fixtures.HabitTrackFixture;
import com.dreamtracker.app.habit.domain.model.Habit;
import com.dreamtracker.app.habit.domain.model.HabitTrackStatus;
import com.dreamtracker.app.infrastructure.exception.EntityNotFoundException;
import com.dreamtracker.app.infrastructure.exception.ExceptionMessages;
import com.dreamtracker.app.infrastructure.utils.DateService;
import com.dreamtracker.app.user.config.CurrentUserProvider;
import com.dreamtracker.app.user.config.MockCurrentUserProviderImpl;
import com.dreamtracker.app.view.domain.model.aggregate.AggregatesFixtures;
import com.dreamtracker.app.view.domain.ports.SingleDayAggregateRepositoryPort;

import java.time.OffsetDateTime;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class DomainSingleDayServiceTest implements AggregatesFixtures, HabitFixture, HabitTrackFixture {

  private DomainSingleDayService domainSingleDayService;
  private final SingleDayAggregateRepositoryPort singleDayAggregateRepositoryPort =
      Mockito.mock(SingleDayAggregateRepositoryPort.class);
  private final CurrentUserProvider currentUserProvider = new MockCurrentUserProviderImpl();
  private final DateService dateService = Mockito.mock(DateService.class);
  private Habit habit = getSampleHabitBuilder(currentUserProvider.getCurrentUser()).build();
  private final OffsetDateTime currentDate = OffsetDateTime.now();
  private final  OffsetDateTime previousDate = currentDate.minusDays(1);

    @BeforeEach
    void setUp() {
    domainSingleDayService =
        new DomainSingleDayService(singleDayAggregateRepositoryPort, dateService);
  }

    @Test
    void initializeAggregate() {
    // given
    var aggregate = getSingleDayAggregateBuilder(habit.getId()).id(null).date(currentDate).build();
    var aggregateSavedToDB = getSingleDayAggregateBuilder(habit.getId()).date(currentDate).build();

    when(dateService.getCurrentDateInISO8601()).thenReturn(currentDate);
    when(singleDayAggregateRepositoryPort.save(aggregate)).thenReturn(aggregateSavedToDB);
    var expected = getSingleDayComponentResponseBuilder(currentDate).build();

    // when
    var actual = domainSingleDayService.initializeAggregate(habit.getId());

    // then
    assertThat(actual).isEqualTo(expected);
  }

  @Test
  void updateAggregatePositiveTestCaseSameDate() {
    // given
    var aggregate = getSingleDayAggregateBuilder(habit.getId()).date(currentDate).build();
    var habitTrackResponse =
        getSampleHabitTrackResponse(currentDate).status(HabitTrackStatus.DONE.toString()).build();
    when(singleDayAggregateRepositoryPort.findByHabitUUID(habit.getId()))
        .thenReturn(Optional.of(aggregate));
    when(dateService.getCurrentDateInISO8601()).thenReturn(currentDate);
    when(singleDayAggregateRepositoryPort.save(aggregate)).thenReturn(aggregate);
    var expected = getSingleDayComponentResponseBuilder(currentDate).most(1).actual(1).build();

    // when
    var actual =
        domainSingleDayService.updateAggregate(
            habit.getId(), habitTrackResponse);

    // then
    assertThat(actual).isEqualTo(expected);
  }

  @Test
  void updateAggregatePositiveTestCaseNewDay() {
    // given
    var aggregate =
        getSingleDayAggregateBuilder(habit.getId())
            .date(previousDate)
            .mostDone(5)
            .actualCount(5)
            .build();
    var habitTrackResponse =
        getSampleHabitTrackResponse(currentDate).status(HabitTrackStatus.DONE.toString()).build();
    when(singleDayAggregateRepositoryPort.findByHabitUUID(habit.getId()))
        .thenReturn(Optional.of(aggregate));
    when(dateService.getCurrentDateInISO8601()).thenReturn(currentDate);
    when(singleDayAggregateRepositoryPort.save(aggregate)).thenReturn(aggregate);
    var expected = getSingleDayComponentResponseBuilder(currentDate).most(5).actual(1).build();

    // when
    var actual =
        domainSingleDayService.updateAggregate(
            habit.getId(), habitTrackResponse);

    // then
    assertThat(actual).isEqualTo(expected);
  }

  @Test
  void updateAggregatePositiveTestNoIncreaseNoDateChange() {
    // given
    var aggregate =
        getSingleDayAggregateBuilder(habit.getId())
            .date(previousDate)
            .mostDone(5)
            .actualCount(5)
            .build();
    var habitTrackResponse =
        getSampleHabitTrackResponse(currentDate).status(HabitTrackStatus.UNDONE.toString()).build();
    when(singleDayAggregateRepositoryPort.findByHabitUUID(habit.getId()))
        .thenReturn(Optional.of(aggregate));
    when(dateService.getCurrentDateInISO8601()).thenReturn(currentDate);
    when(singleDayAggregateRepositoryPort.save(aggregate)).thenReturn(aggregate);
    var expected = getSingleDayComponentResponseBuilder(previousDate).most(5).actual(5).build();
    // when
    var actual =
        domainSingleDayService.updateAggregate(
            habit.getId(), habitTrackResponse);

    // then
    assertThat(actual).isEqualTo(expected);
  }

  @Test
  void updateAggregatePositiveTestNewBest() {
    // given
    var aggregate =
        getSingleDayAggregateBuilder(habit.getId())
            .date(currentDate)
            .mostDone(5)
            .actualCount(5)
            .build();
    var habitTrackResponse = getSampleHabitTrackResponse(currentDate).build();
    when(singleDayAggregateRepositoryPort.findByHabitUUID(habit.getId()))
        .thenReturn(Optional.of(aggregate));
    when(dateService.getCurrentDateInISO8601()).thenReturn(currentDate);
    when(singleDayAggregateRepositoryPort.save(aggregate)).thenReturn(aggregate);
    var expected = getSingleDayComponentResponseBuilder(currentDate).most(6).actual(6).build();
    // when
    var actual =
        domainSingleDayService.updateAggregate(
            habit.getId(), habitTrackResponse);

    // then
    assertThat(actual).isEqualTo(expected);
  }

  @Test
  void updateAggregateEntityNotFoundException() {
    // given
    var aggregate =
        getSingleDayAggregateBuilder(habit.getId())
            .date(currentDate)
            .mostDone(5)
            .actualCount(5)
            .build();
    var habitTrackResponse = getSampleHabitTrackResponse(currentDate).build();
    when(singleDayAggregateRepositoryPort.findByHabitUUID(habit.getId()))
        .thenReturn(Optional.empty());
    when(dateService.getCurrentDateInISO8601()).thenReturn(currentDate);
    when(singleDayAggregateRepositoryPort.save(aggregate)).thenReturn(aggregate);
    // when
    assertThatThrownBy(
            () -> {
              domainSingleDayService.updateAggregate(
                  habit.getId(), habitTrackResponse);
            })
            // then
        .isInstanceOf(EntityNotFoundException.class)
        .hasMessage(ExceptionMessages.entityNotFoundExceptionMessage);
  }

    @Test
    void getAggregatePositiveTestCase() {
        // given
        var aggregate =
                getSingleDayAggregateBuilder(habit.getId())
                        .date(currentDate)
                        .mostDone(5)
                        .actualCount(5)
                        .build();
        when(singleDayAggregateRepositoryPort.findByHabitUUID(habit.getId()))
                .thenReturn(Optional.of(aggregate));
        var expected = getSingleDayComponentResponseBuilder(currentDate).most(5).actual(5).build();
        // when
        var actual =
                domainSingleDayService.getAggregate(
                        habit.getId());

        // then
        assertThat(actual).isEqualTo(expected);
      }

    @Test
    void getCalculatedResponseEntityNotFoundException() {
        // given
        var aggregate =
                getSingleDayAggregateBuilder(habit.getId())
                        .date(currentDate)
                        .mostDone(5)
                        .actualCount(5)
                        .build();
        when(singleDayAggregateRepositoryPort.findByHabitUUID(habit.getId()))
                .thenReturn(Optional.empty());
        when(dateService.getCurrentDateInISO8601()).thenReturn(currentDate);
        when(singleDayAggregateRepositoryPort.save(aggregate)).thenReturn(aggregate);
        // when
        assertThatThrownBy(
                () -> {
                    domainSingleDayService.getAggregate(
                            habit.getId());
                })
                // then
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessage(ExceptionMessages.entityNotFoundExceptionMessage);
    }
}