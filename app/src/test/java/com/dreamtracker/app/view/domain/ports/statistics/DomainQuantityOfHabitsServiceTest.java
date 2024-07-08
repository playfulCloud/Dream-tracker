package com.dreamtracker.app.view.domain.ports.statistics;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.Mockito.when;

import com.dreamtracker.app.habit.domain.fixtures.HabitFixture;
import com.dreamtracker.app.habit.domain.fixtures.HabitTrackFixture;
import com.dreamtracker.app.habit.domain.model.Habit;
import com.dreamtracker.app.habit.domain.utils.HabitTrackStatus;
import com.dreamtracker.app.infrastructure.exception.EntityNotFoundException;
import com.dreamtracker.app.infrastructure.exception.ExceptionMessages;
import com.dreamtracker.app.infrastructure.utils.DateService;
import com.dreamtracker.app.user.config.CurrentUserProvider;
import com.dreamtracker.app.user.config.MockCurrentUserProviderImpl;
import com.dreamtracker.app.view.adapters.api.QuantityOfHabitsComponentResponse;
import com.dreamtracker.app.view.domain.model.aggregate.AggregatesFixtures;
import com.dreamtracker.app.view.domain.model.aggregate.QuantityOfHabitsAggregate;
import com.dreamtracker.app.view.domain.ports.QuantityOfHabitsAggregateRepositoryPort;
import java.util.Optional;
import java.util.stream.Stream;
import lombok.Builder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mockito;

class DomainQuantityOfHabitsServiceTest
    implements HabitFixture, AggregatesFixtures, HabitTrackFixture {

  private final QuantityOfHabitsAggregateRepositoryPort quantityOfHabitsAggregateRepositoryPort =
      Mockito.mock(QuantityOfHabitsAggregateRepositoryPort.class);
  private final CurrentUserProvider currentUserProvider = new MockCurrentUserProviderImpl();
  private DomainQuantityOfHabitsService domainQuantityOfHabitsService;
  private final DateService dateService = new DateService();
  private Habit habit;

    @BeforeEach
    void setUp() {
    domainQuantityOfHabitsService =
        new DomainQuantityOfHabitsService(quantityOfHabitsAggregateRepositoryPort);
    habit = getSampleHabitBuilder(currentUserProvider.getCurrentUser()).build();
      }

    @Test
    void initializeAggregates() {
    // given
    var aggregate = getQuantityOfHabitsAggregateBuilder(habit.getId()).id(null).build();
    var aggregateSavedToDB = getQuantityOfHabitsAggregateBuilder(habit.getId()).build();
    var expectedComponentResponse = getQuantityOfHabitsComponentResponse().build();

    when(quantityOfHabitsAggregateRepositoryPort.save(aggregate)).thenReturn(aggregateSavedToDB);
    // when
    var actualComponentResponse = domainQuantityOfHabitsService.initializeAggregates(habit.getId());
    // then
    assertThat(actualComponentResponse).isEqualTo(expectedComponentResponse);
      }

  // Adopted convention: After receiving HabitTrackResponse with DONE status the trend would be
  // according to the method below.
  private static ArgumentHolder getQuantityOfHabitsComponentResponseBasedOnTrendStatusDone(
      DomainQuantityOfHabitsService.TrendStatus status) {
    ArgumentHolder argumentHolder = null;
    switch (status) {
      case SLOW_FALLING:
        argumentHolder =
            ArgumentHolder.builder()
                .aggregate(
                    QuantityOfHabitsAggregate.builder()
                        .doneHabits(0)
                        .unDoneHabits(2)
                        .currentTrend(-2)
                        .build())
                .response(
                    QuantityOfHabitsComponentResponse.builder()
                        .trend(DomainQuantityOfHabitsService.TrendStatus.SLOW_FALLING.toString())
                        .undone(2)
                        .done(1)
                        .build())
                .build();
        break;
      case SLOW_RISING:
        argumentHolder =
            ArgumentHolder.builder()
                .aggregate(
                    QuantityOfHabitsAggregate.builder()
                        .doneHabits(0)
                        .unDoneHabits(0)
                        .currentTrend(0)
                        .build())
                .response(
                    QuantityOfHabitsComponentResponse.builder()
                        .trend(DomainQuantityOfHabitsService.TrendStatus.SLOW_RISING.toString())
                        .undone(0)
                        .done(1)
                        .build())
                .build();
        break;
      case RISING:
        argumentHolder =
            ArgumentHolder.builder()
                .aggregate(
                    QuantityOfHabitsAggregate.builder()
                        .doneHabits(2)
                        .unDoneHabits(0)
                        .currentTrend(2)
                        .build())
                .response(
                    QuantityOfHabitsComponentResponse.builder()
                        .trend(DomainQuantityOfHabitsService.TrendStatus.RISING.toString())
                        .undone(0)
                        .done(3)
                        .build())
                .build();
        break;
      case FALLING:
        argumentHolder =
            ArgumentHolder.builder()
                .aggregate(
                    QuantityOfHabitsAggregate.builder()
                        .doneHabits(0)
                        .unDoneHabits(5)
                        .currentTrend(-5)
                        .build())
                .response(
                    QuantityOfHabitsComponentResponse.builder()
                        .trend(DomainQuantityOfHabitsService.TrendStatus.FALLING.toString())
                        .undone(5)
                        .done(1)
                        .build())
                .build();
        break;
      case FAST_FALLING:
        argumentHolder =
            ArgumentHolder.builder()
                .aggregate(
                    QuantityOfHabitsAggregate.builder()
                        .doneHabits(0)
                        .unDoneHabits(7)
                        .currentTrend(-7)
                        .build())
                .response(
                    QuantityOfHabitsComponentResponse.builder()
                        .trend(DomainQuantityOfHabitsService.TrendStatus.FAST_FALLING.toString())
                        .undone(7)
                        .done(1)
                        .build())
                .build();
        break;
      case FAST_RISING:
        argumentHolder =
            ArgumentHolder.builder()
                .aggregate(
                    QuantityOfHabitsAggregate.builder()
                        .doneHabits(4)
                        .unDoneHabits(0)
                        .currentTrend(4)
                        .build())
                .response(
                    QuantityOfHabitsComponentResponse.builder()
                        .trend(DomainQuantityOfHabitsService.TrendStatus.FAST_RISING.toString())
                        .undone(0)
                        .done(5)
                        .build())
                .build();
        break;
      case POSITIVE_PLATEAU:
        argumentHolder =
            ArgumentHolder.builder()
                .aggregate(
                    QuantityOfHabitsAggregate.builder()
                        .doneHabits(6)
                        .unDoneHabits(0)
                        .currentTrend(6)
                        .build())
                .response(
                    QuantityOfHabitsComponentResponse.builder()
                        .trend(
                            DomainQuantityOfHabitsService.TrendStatus.POSITIVE_PLATEAU.toString())
                        .undone(0)
                        .done(7)
                        .build())
                .build();
        break;
      case NEGATIVE_PLATEAU:
        argumentHolder =
            ArgumentHolder.builder()
                .aggregate(
                    QuantityOfHabitsAggregate.builder()
                        .doneHabits(0)
                        .unDoneHabits(8)
                        .currentTrend(-8)
                        .build())
                .response(
                    QuantityOfHabitsComponentResponse.builder()
                        .trend(
                            DomainQuantityOfHabitsService.TrendStatus.NEGATIVE_PLATEAU.toString())
                        .undone(8)
                        .done(1)
                        .build())
                .build();
        break;
      case STAGNATION:
        argumentHolder =
            ArgumentHolder.builder()
                .aggregate(
                    QuantityOfHabitsAggregate.builder()
                        .doneHabits(0)
                        .unDoneHabits(1)
                        .currentTrend(-1)
                        .build())
                .response(
                    QuantityOfHabitsComponentResponse.builder()
                        .trend(DomainQuantityOfHabitsService.TrendStatus.STAGNATION.toString())
                        .undone(1)
                        .done(1)
                        .build())
                .build();
        break;
    }
    return argumentHolder;
  }

  // Adopted convention: After receiving HabitTrackResponse with UNDONE status the trend would be
  // according to the method below.
  private static ArgumentHolder getQuantityOfHabitsComponentResponseBasedOnTrendStatusUnDone(
      DomainQuantityOfHabitsService.TrendStatus status) {
    ArgumentHolder argumentHolder = null;
    switch (status) {
      case SLOW_FALLING:
        argumentHolder =
            ArgumentHolder.builder()
                .aggregate(
                    QuantityOfHabitsAggregate.builder()
                        .doneHabits(0)
                        .unDoneHabits(0)
                        .currentTrend(0)
                        .build())
                .response(
                    QuantityOfHabitsComponentResponse.builder()
                        .trend(DomainQuantityOfHabitsService.TrendStatus.SLOW_FALLING.toString())
                        .undone(1)
                        .done(0)
                        .build())
                .build();
        break;
      case SLOW_RISING:
        argumentHolder =
            ArgumentHolder.builder()
                .aggregate(
                    QuantityOfHabitsAggregate.builder()
                        .doneHabits(3)
                        .unDoneHabits(0)
                        .currentTrend(3)
                        .build())
                .response(
                    QuantityOfHabitsComponentResponse.builder()
                        .trend(DomainQuantityOfHabitsService.TrendStatus.SLOW_RISING.toString())
                        .undone(1)
                        .done(3)
                        .build())
                .build();
        break;
      case RISING:
        argumentHolder =
            ArgumentHolder.builder()
                .aggregate(
                    QuantityOfHabitsAggregate.builder()
                        .doneHabits(5)
                        .unDoneHabits(0)
                        .currentTrend(5)
                        .build())
                .response(
                    QuantityOfHabitsComponentResponse.builder()
                        .trend(DomainQuantityOfHabitsService.TrendStatus.RISING.toString())
                        .undone(1)
                        .done(5)
                        .build())
                .build();
        break;
      case FALLING:
        argumentHolder =
            ArgumentHolder.builder()
                .aggregate(
                    QuantityOfHabitsAggregate.builder()
                        .doneHabits(0)
                        .unDoneHabits(2)
                        .currentTrend(-2)
                        .build())
                .response(
                    QuantityOfHabitsComponentResponse.builder()
                        .trend(DomainQuantityOfHabitsService.TrendStatus.FALLING.toString())
                        .undone(3)
                        .done(0)
                        .build())
                .build();
        break;
      case FAST_FALLING:
        argumentHolder =
            ArgumentHolder.builder()
                .aggregate(
                    QuantityOfHabitsAggregate.builder()
                        .doneHabits(0)
                        .unDoneHabits(4)
                        .currentTrend(-4)
                        .build())
                .response(
                    QuantityOfHabitsComponentResponse.builder()
                        .trend(DomainQuantityOfHabitsService.TrendStatus.FAST_FALLING.toString())
                        .undone(5)
                        .done(0)
                        .build())
                .build();
        break;
      case FAST_RISING:
        argumentHolder =
            ArgumentHolder.builder()
                .aggregate(
                    QuantityOfHabitsAggregate.builder()
                        .doneHabits(7)
                        .unDoneHabits(0)
                        .currentTrend(7)
                        .build())
                .response(
                    QuantityOfHabitsComponentResponse.builder()
                        .trend(DomainQuantityOfHabitsService.TrendStatus.FAST_RISING.toString())
                        .undone(1)
                        .done(7)
                        .build())
                .build();
        break;
      case POSITIVE_PLATEAU:
        argumentHolder =
            ArgumentHolder.builder()
                .aggregate(
                    QuantityOfHabitsAggregate.builder()
                        .doneHabits(8)
                        .unDoneHabits(0)
                        .currentTrend(8)
                        .build())
                .response(
                    QuantityOfHabitsComponentResponse.builder()
                        .trend(
                            DomainQuantityOfHabitsService.TrendStatus.POSITIVE_PLATEAU.toString())
                        .undone(1)
                        .done(8)
                        .build())
                .build();
        break;
      case NEGATIVE_PLATEAU:
        argumentHolder =
            ArgumentHolder.builder()
                .aggregate(
                    QuantityOfHabitsAggregate.builder()
                        .doneHabits(0)
                        .unDoneHabits(6)
                        .currentTrend(-6)
                        .build())
                .response(
                    QuantityOfHabitsComponentResponse.builder()
                        .trend(
                            DomainQuantityOfHabitsService.TrendStatus.NEGATIVE_PLATEAU.toString())
                        .undone(7)
                        .done(0)
                        .build())
                .build();
        break;
      case STAGNATION:
        argumentHolder =
            ArgumentHolder.builder()
                .aggregate(
                    QuantityOfHabitsAggregate.builder()
                        .doneHabits(1)
                        .unDoneHabits(0)
                        .currentTrend(1)
                        .build())
                .response(
                    QuantityOfHabitsComponentResponse.builder()
                        .trend(DomainQuantityOfHabitsService.TrendStatus.STAGNATION.toString())
                        .undone(1)
                        .done(1)
                        .build())
                .build();
        break;
    }
    return argumentHolder;
  }

  private static Stream<Arguments> argumentsBasedOnTrendDone() {
    return Stream.of(
        Arguments.of(
            getQuantityOfHabitsComponentResponseBasedOnTrendStatusDone(
                DomainQuantityOfHabitsService.TrendStatus.SLOW_RISING),
            HabitTrackStatus.DONE.toString()),
        Arguments.of(
            getQuantityOfHabitsComponentResponseBasedOnTrendStatusDone(
                DomainQuantityOfHabitsService.TrendStatus.SLOW_FALLING),
            HabitTrackStatus.DONE.toString()),
        Arguments.of(
            getQuantityOfHabitsComponentResponseBasedOnTrendStatusDone(
                DomainQuantityOfHabitsService.TrendStatus.RISING),
            HabitTrackStatus.DONE.toString()),
        Arguments.of(
            getQuantityOfHabitsComponentResponseBasedOnTrendStatusDone(
                DomainQuantityOfHabitsService.TrendStatus.FALLING),
            HabitTrackStatus.DONE.toString()),
        Arguments.of(
            getQuantityOfHabitsComponentResponseBasedOnTrendStatusDone(
                DomainQuantityOfHabitsService.TrendStatus.FAST_RISING),
            HabitTrackStatus.DONE.toString()),
        Arguments.of(
            getQuantityOfHabitsComponentResponseBasedOnTrendStatusDone(
                DomainQuantityOfHabitsService.TrendStatus.FAST_FALLING),
            HabitTrackStatus.DONE.toString()),
        Arguments.of(
            getQuantityOfHabitsComponentResponseBasedOnTrendStatusDone(
                DomainQuantityOfHabitsService.TrendStatus.POSITIVE_PLATEAU),
            HabitTrackStatus.DONE.toString()),
        Arguments.of(
            getQuantityOfHabitsComponentResponseBasedOnTrendStatusDone(
                DomainQuantityOfHabitsService.TrendStatus.NEGATIVE_PLATEAU),
            HabitTrackStatus.DONE.toString()),
        Arguments.of(
            getQuantityOfHabitsComponentResponseBasedOnTrendStatusDone(
                DomainQuantityOfHabitsService.TrendStatus.STAGNATION),
            HabitTrackStatus.DONE.toString()));
  }

  private static Stream<Arguments> argumentsBasedOnTrendUndone() {
    return Stream.of(
            Arguments.of(
                    getQuantityOfHabitsComponentResponseBasedOnTrendStatusUnDone(
                            DomainQuantityOfHabitsService.TrendStatus.SLOW_RISING),
                    HabitTrackStatus.UNDONE.toString()),
            Arguments.of(
                    getQuantityOfHabitsComponentResponseBasedOnTrendStatusUnDone(
                            DomainQuantityOfHabitsService.TrendStatus.SLOW_FALLING),
                    HabitTrackStatus.UNDONE.toString()),
            Arguments.of(
                    getQuantityOfHabitsComponentResponseBasedOnTrendStatusUnDone(
                            DomainQuantityOfHabitsService.TrendStatus.RISING),
                    HabitTrackStatus.UNDONE.toString()),
            Arguments.of(
                    getQuantityOfHabitsComponentResponseBasedOnTrendStatusUnDone(
                            DomainQuantityOfHabitsService.TrendStatus.FALLING),
                    HabitTrackStatus.UNDONE.toString()),
            Arguments.of(
                    getQuantityOfHabitsComponentResponseBasedOnTrendStatusUnDone(
                            DomainQuantityOfHabitsService.TrendStatus.FAST_RISING),
                    HabitTrackStatus.UNDONE.toString()),
            Arguments.of(
                    getQuantityOfHabitsComponentResponseBasedOnTrendStatusUnDone(
                            DomainQuantityOfHabitsService.TrendStatus.FAST_FALLING),
                    HabitTrackStatus.UNDONE.toString()),
            Arguments.of(
                    getQuantityOfHabitsComponentResponseBasedOnTrendStatusUnDone(
                            DomainQuantityOfHabitsService.TrendStatus.POSITIVE_PLATEAU),
                    HabitTrackStatus.UNDONE.toString()),
            Arguments.of(
                    getQuantityOfHabitsComponentResponseBasedOnTrendStatusUnDone(
                            DomainQuantityOfHabitsService.TrendStatus.NEGATIVE_PLATEAU),
                    HabitTrackStatus.UNDONE.toString()),
            Arguments.of(
                    getQuantityOfHabitsComponentResponseBasedOnTrendStatusUnDone(
                            DomainQuantityOfHabitsService.TrendStatus.STAGNATION),
                    HabitTrackStatus.UNDONE.toString()));
  }

  private static Stream<Arguments> argumentsBasedOnStatus() {
    return Stream.concat(argumentsBasedOnTrendDone(), argumentsBasedOnTrendUndone());
  }

  @Builder
  private record ArgumentHolder(
      QuantityOfHabitsAggregate aggregate, QuantityOfHabitsComponentResponse response) {}

  @ParameterizedTest
  @MethodSource("argumentsBasedOnStatus")
  void updateAggregatesAndCalculateResponsePositiveTestCaseDone(
      ArgumentHolder argumentHolder, String status) {
    // given

    var quantityOfHabitsAggregate = argumentHolder.aggregate;
    var expected = argumentHolder.response;

    var habitTrackResponse =
        getSampleHabitTrackResponse(dateService.getCurrentDateInISO8601()).status(status).build();

    when(quantityOfHabitsAggregateRepositoryPort.findByHabitUUID(habit.getId()))
        .thenReturn(Optional.of(quantityOfHabitsAggregate));
    when(quantityOfHabitsAggregateRepositoryPort.save(quantityOfHabitsAggregate))
        .thenReturn(quantityOfHabitsAggregate);

    // when
    var actual =
        domainQuantityOfHabitsService.updateAggregatesAndCalculateResponse(
            habit.getId(), habitTrackResponse);

    // then
    assertThat(actual).isEqualTo(expected);
  }

  @Test
  void updateAggregatesAndCalculateResponseEntityNotFoundException() {
    // given
    var habitTrackResponse =
        getSampleHabitTrackResponse(dateService.getCurrentDateInISO8601())
            .status(HabitTrackStatus.DONE.toString())
            .build();

    when(quantityOfHabitsAggregateRepositoryPort.findByHabitUUID(habit.getId()))
        .thenReturn(Optional.empty());

    assertThatThrownBy(
            () -> {
              // when
              domainQuantityOfHabitsService.updateAggregatesAndCalculateResponse(
                  habit.getId(), habitTrackResponse);
              // then
            })
        .isInstanceOf(EntityNotFoundException.class)
        .hasMessage(ExceptionMessages.entityNotFoundExceptionMessage);
  }

  @Test
  void getCalculateResponsePositiveTestCase() {
    // given
    var quantityOfHabitsAggregate = getQuantityOfHabitsAggregateBuilder(habit.getId()).build();

    when(quantityOfHabitsAggregateRepositoryPort.findByHabitUUID(habit.getId()))
        .thenReturn(Optional.of(quantityOfHabitsAggregate));

    var expectedResponse = getQuantityOfHabitsComponentResponse().build();

    // when
    var actual = domainQuantityOfHabitsService.getCalculateResponse(habit.getId());
    // then
    assertThat(expectedResponse).isEqualTo(actual);
  }

  @Test
  void getCalculateResponseEntityNotFoundException() {
    // given
    when(quantityOfHabitsAggregateRepositoryPort.findByHabitUUID(habit.getId()))
        .thenReturn(Optional.empty());

    assertThatThrownBy(
            () -> {
              // when
              domainQuantityOfHabitsService.getCalculateResponse(habit.getId());
              // then
            })
        .isInstanceOf(EntityNotFoundException.class)
        .hasMessage(ExceptionMessages.entityNotFoundExceptionMessage);
  }
}