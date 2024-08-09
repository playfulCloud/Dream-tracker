package com.dreamtracker.app.view.domain.ports.statistics;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.Mockito.*;

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
import com.dreamtracker.app.view.domain.ports.BreaksAggregateRepositoryPort;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class DomainBreaksServiceTest implements AggregatesFixtures, HabitFixture, HabitTrackFixture {

  private final BreaksAggregateRepositoryPort breaksAggregateRepositoryPort =
      Mockito.mock(BreaksAggregateRepositoryPort.class);
  private final CurrentUserProvider currentUserProvider = new MockCurrentUserProviderImpl();
  private DomainBreaksService domainBreaksService;
  private final DateService dateService = new DateService();
  private Habit habit;

  @BeforeEach
  void setUp() {
    habit = getSampleHabitBuilder(currentUserProvider.getCurrentFromSecurityContext()).build();
    domainBreaksService = new DomainBreaksService(breaksAggregateRepositoryPort);
  }
  @Test
  void initializeAggregatePositiveTestCase() {
    // given
    var aggregate = getBreakAggregateBuilder(habit.getId()).id(null).build();
    var aggregateSavedToDB = getBreakAggregateBuilder(habit.getId()).build();
    var expectedComponentResponse = getBreakStatsComponentResponse().build();

    when(breaksAggregateRepositoryPort.save(aggregate)).thenReturn(aggregateSavedToDB);
    // when
    var actualComponentResponse = domainBreaksService.initializeAggregate(habit.getId());
    // then
    assertThat(actualComponentResponse).isEqualTo(expectedComponentResponse);
  }

  @Test
  void updateAggregatePositiveTestCaseDone() {
    // given
    var breakAggregateSavedToDB = getBreakAggregateBuilder(habit.getId()).build();
    var habitTrackResponse =
        getSampleHabitTrackResponse(dateService.getCurrentDateInISO8601()).build();

    when(breaksAggregateRepositoryPort.findByHabitUUID(habit.getId()))
        .thenReturn(Optional.of(breakAggregateSavedToDB));
    when(breaksAggregateRepositoryPort.save(breakAggregateSavedToDB))
        .thenReturn(breakAggregateSavedToDB);

    var expected = getBreakStatsComponentResponse().averageBreak(0).build();

    // when
    var actual =
        domainBreaksService.updateAggregate(habit.getId(), habitTrackResponse);

    // then
    assertThat(actual).isEqualTo(expected);
  }

  @Test
  void updateAggregatePositiveTestCaseUndone() {
    // given
    var breakAggregate = getBreakAggregateBuilder(habit.getId()).build();
    var habitTrackResponse =
        getSampleHabitTrackResponse(dateService.getCurrentDateInISO8601())
            .status(String.valueOf(HabitTrackStatus.UNDONE))
            .build();

    when(breaksAggregateRepositoryPort.findByHabitUUID(habit.getId()))
        .thenReturn(Optional.of(breakAggregate));
    when(breaksAggregateRepositoryPort.save(breakAggregate)).thenReturn(breakAggregate);

    var expected = getBreakStatsComponentResponse().averageBreak(1).build();

    // when
    var actual =
        domainBreaksService.updateAggregate(habit.getId(), habitTrackResponse);

    // then
    assertThat(actual).isEqualTo(expected);
  }

  // The main goal of these test is check proper calculation during break increment only of
  // sumOfBreaks
  @Test
  void updateAggregatePositiveTestCaseisBreakTrue() {
    // given
    var breakAggregateSavedToDB =
        getBreakAggregateBuilder(habit.getId())
            .breaksQuantity(10)
            .sumOfBreaks(49)
            .isBreak(true)
            .build();
    var habitTrackResponse =
        getSampleHabitTrackResponse(dateService.getCurrentDateInISO8601())
            .status(String.valueOf(HabitTrackStatus.UNDONE))
            .build();

    when(breaksAggregateRepositoryPort.findByHabitUUID(habit.getId()))
        .thenReturn(Optional.of(breakAggregateSavedToDB));
    when(breaksAggregateRepositoryPort.save(breakAggregateSavedToDB))
        .thenReturn(breakAggregateSavedToDB);

    var expected = getBreakStatsComponentResponse().averageBreak(5.0).build();
    // when
    var actual =
        domainBreaksService.updateAggregate(habit.getId(), habitTrackResponse);

    // then
    assertThat(actual).isEqualTo(expected);
  }

  // The main goal of these test is check proper calculation during break increment sumOfBreaks,
  // quantityOfBreaks;
  @Test
  void updateAggregatePositiveTestCaseisBreakFalse() {
    // given
    var breakAggregateSavedToDB =
        getBreakAggregateBuilder(habit.getId())
            .breaksQuantity(9)
            .sumOfBreaks(49)
            .isBreak(false)
            .build();
    var habitTrackResponse =
        getSampleHabitTrackResponse(dateService.getCurrentDateInISO8601())
            .status(String.valueOf(HabitTrackStatus.UNDONE))
            .build();

    when(breaksAggregateRepositoryPort.findByHabitUUID(habit.getId()))
        .thenReturn(Optional.of(breakAggregateSavedToDB));
    when(breaksAggregateRepositoryPort.save(breakAggregateSavedToDB))
        .thenReturn(breakAggregateSavedToDB);

    var expected = getBreakStatsComponentResponse().averageBreak(5.0).build();
    // when
    var actual =
        domainBreaksService.updateAggregate(habit.getId(), habitTrackResponse);

    // then
    assertThat(actual).isEqualTo(expected);
  }

  @Test
  void updateAggregateEntityNotFoundException() {
    // given
    var habitTrackResponse =
        getSampleHabitTrackResponse(dateService.getCurrentDateInISO8601()).build();
    when(breaksAggregateRepositoryPort.findByHabitUUID(habit.getId())).thenReturn(Optional.empty());
    assertThatThrownBy(
            () -> {
              // when
              domainBreaksService.updateAggregate(
                  habit.getId(), habitTrackResponse);
              // then
            })
        .isInstanceOf(EntityNotFoundException.class)
        .hasMessage(ExceptionMessages.entityNotFoundExceptionMessage);
  }

  @Test
  void getAggregatePositiveTestCase() {
    // given
    var habit = getSampleHabitBuilder(currentUserProvider.getCurrentFromSecurityContext()).build();
    var breakAggregateSavedToDB =
        getBreakAggregateBuilder(habit.getId())
            .breaksQuantity(0)
            .sumOfBreaks(0)
            .isBreak(false)
            .build();

    when(breaksAggregateRepositoryPort.findByHabitUUID(habit.getId()))
        .thenReturn(Optional.of(breakAggregateSavedToDB));

    var expected = getBreakStatsComponentResponse().averageBreak(0).build();
    // when
    var actual = domainBreaksService.getAggregate(habit.getId());

    // then
    assertThat(actual).isEqualTo(expected);
  }

  @Test
  void getAggregateEntityNotFoundException() {
    // given
    when(breaksAggregateRepositoryPort.findByHabitUUID(habit.getId())).thenReturn(Optional.empty());

    assertThatThrownBy(
            () -> {
              // when
              domainBreaksService.getAggregate(habit.getId());
              // then
            })
        .isInstanceOf(EntityNotFoundException.class)
        .hasMessage(ExceptionMessages.entityNotFoundExceptionMessage);
  }
}
