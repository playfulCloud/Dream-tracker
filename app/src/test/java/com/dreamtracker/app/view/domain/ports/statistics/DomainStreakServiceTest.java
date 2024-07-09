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
import com.dreamtracker.app.view.domain.model.aggregate.AggregatesFixtures;
import com.dreamtracker.app.view.domain.ports.StreakAggregateRepositoryPort;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class DomainStreakServiceTest implements HabitFixture, AggregatesFixtures, HabitTrackFixture {

    private DomainStreakService domainStreakService;
    private final StreakAggregateRepositoryPort streakAggregateRepositoryPort = Mockito.mock(StreakAggregateRepositoryPort.class);
    private final CurrentUserProvider currentUserProvider = new MockCurrentUserProviderImpl();
  private final DateService dateService = new DateService();
  private Habit habit = getSampleHabitBuilder(currentUserProvider.getCurrentUser()).build();

    @BeforeEach
    void setUp() {
        domainStreakService = new DomainStreakService(streakAggregateRepositoryPort);
      }

    @Test
    void initializeAggregates() {
    // given
    var aggregate = getStreakAggregateBuilder(habit.getId()).id(null).build();
    var aggregateSavedToDB = getStreakAggregateBuilder(habit.getId()).build();

    when(streakAggregateRepositoryPort.save(aggregate)).thenReturn(aggregateSavedToDB);
    var expected = getStreakComponentResponseBuilder().build();

    // when
    var actual = domainStreakService.initializeAggregates(habit.getId());

    // then
    assertThat(actual).isEqualTo(expected);
  }

  @Test
  void updateAggregatesAndCalculateResponsePositiveTestCaseDoneStreakIncrease() {
    // given
    var aggregateSavedToDB = getStreakAggregateBuilder(habit.getId()).build();
    var habitTrackResponse =
        getSampleHabitTrackResponse(dateService.getCurrentDateInISO8601())
            .status(HabitTrackStatus.DONE.toString())
            .build();

    when(streakAggregateRepositoryPort.findByHabitUUID(habit.getId()))
        .thenReturn(Optional.of(aggregateSavedToDB));
    when(streakAggregateRepositoryPort.save(aggregateSavedToDB)).thenReturn(aggregateSavedToDB);
    var expected = getStreakComponentResponseBuilder().longest(1).actual(1).build();

    // when
    var actual =
        domainStreakService.updateAggregatesAndCalculateResponse(habit.getId(), habitTrackResponse);

    // then
    assertThat(actual).isEqualTo(expected);
  }

  @Test
  void updateAggregatesAndCalculateResponsePositiveTestCaseUnDoneStreakBreaks() {
    // given
    var aggregateSavedToDB =
        getStreakAggregateBuilder(habit.getId()).currentStreak(10).longestStreak(10).build();
    var habitTrackResponse =
        getSampleHabitTrackResponse(dateService.getCurrentDateInISO8601())
            .status(HabitTrackStatus.UNDONE.toString())
            .build();

    when(streakAggregateRepositoryPort.findByHabitUUID(habit.getId()))
        .thenReturn(Optional.of(aggregateSavedToDB));
    when(streakAggregateRepositoryPort.save(aggregateSavedToDB)).thenReturn(aggregateSavedToDB);
    var expected = getStreakComponentResponseBuilder().longest(10).actual(0).build();

    // when
    var actual =
        domainStreakService.updateAggregatesAndCalculateResponse(habit.getId(), habitTrackResponse);

    // then
    assertThat(actual).isEqualTo(expected);
  }

  @Test
  void updateAggregatesAndCalculateResponsePositiveTestCaseUnDoneStreakStaysTheSame() {
    // given
    var aggregateSavedToDB =
        getStreakAggregateBuilder(habit.getId()).currentStreak(0).longestStreak(10).build();
    var habitTrackResponse =
        getSampleHabitTrackResponse(dateService.getCurrentDateInISO8601())
            .status(HabitTrackStatus.UNDONE.toString())
            .build();

    when(streakAggregateRepositoryPort.findByHabitUUID(habit.getId()))
        .thenReturn(Optional.of(aggregateSavedToDB));
    when(streakAggregateRepositoryPort.save(aggregateSavedToDB)).thenReturn(aggregateSavedToDB);
    var expected = getStreakComponentResponseBuilder().longest(10).actual(0).build();

    // when
    var actual =
        domainStreakService.updateAggregatesAndCalculateResponse(habit.getId(), habitTrackResponse);

    // then
    assertThat(actual).isEqualTo(expected);
  }

  @Test
  void updateAggregatesAndCalculateResponsePositiveTestCaseDoneStreakStaysTheSame() {
    // given
    var aggregateSavedToDB =
        getStreakAggregateBuilder(habit.getId()).currentStreak(0).longestStreak(10).build();
    var habitTrackResponse =
        getSampleHabitTrackResponse(dateService.getCurrentDateInISO8601())
            .status(HabitTrackStatus.DONE.toString())
            .build();

    when(streakAggregateRepositoryPort.findByHabitUUID(habit.getId()))
        .thenReturn(Optional.of(aggregateSavedToDB));
    when(streakAggregateRepositoryPort.save(aggregateSavedToDB)).thenReturn(aggregateSavedToDB);
    var expected = getStreakComponentResponseBuilder().longest(10).actual(1).build();

    // when
    var actual =
        domainStreakService.updateAggregatesAndCalculateResponse(habit.getId(), habitTrackResponse);

    // then
    assertThat(actual).isEqualTo(expected);
  }

  @Test
  void updateAggregatesAndCalculateResponsePositiveTestCaseDoneStreakChanges() {
    // given
    var aggregateSavedToDB =
        getStreakAggregateBuilder(habit.getId()).currentStreak(10).longestStreak(10).build();
    var habitTrackResponse =
        getSampleHabitTrackResponse(dateService.getCurrentDateInISO8601())
            .status(HabitTrackStatus.DONE.toString())
            .build();

    when(streakAggregateRepositoryPort.findByHabitUUID(habit.getId()))
        .thenReturn(Optional.of(aggregateSavedToDB));
    when(streakAggregateRepositoryPort.save(aggregateSavedToDB)).thenReturn(aggregateSavedToDB);
    var expected = getStreakComponentResponseBuilder().longest(11).actual(11).build();

    // when
    var actual =
        domainStreakService.updateAggregatesAndCalculateResponse(habit.getId(), habitTrackResponse);

    // then
    assertThat(actual).isEqualTo(expected);
  }

  @Test
  void updateAggregatesAndCalculateResponseEntityNotFoundEx() {
    // given
    var habitTrackResponse =
        getSampleHabitTrackResponse(dateService.getCurrentDateInISO8601())
            .status(HabitTrackStatus.DONE.toString())
            .build();

    when(streakAggregateRepositoryPort.findByHabitUUID(habit.getId())).thenReturn(Optional.empty());

    assertThatThrownBy(
            () -> {
              // when
              domainStreakService.updateAggregatesAndCalculateResponse(
                  habit.getId(), habitTrackResponse);
              // then
            })
        .isInstanceOf(EntityNotFoundException.class)
        .hasMessage(ExceptionMessages.entityNotFoundExceptionMessage);
  }

    @Test
    void getCalculateResponsePositiveTestCase() {
        // given
        var aggregateSavedToDB =
                getStreakAggregateBuilder(habit.getId()).build();

        when(streakAggregateRepositoryPort.findByHabitUUID(habit.getId()))
                .thenReturn(Optional.of(aggregateSavedToDB));
        when(streakAggregateRepositoryPort.save(aggregateSavedToDB)).thenReturn(aggregateSavedToDB);
        var expected = getStreakComponentResponseBuilder().build();

        // when
        var actual =
                domainStreakService.getCalculateResponse(habit.getId());
        // then
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void getCalculateResponseEntityNotFoundException() {
// given
        when(streakAggregateRepositoryPort.findByHabitUUID(habit.getId())).thenReturn(Optional.empty());
        assertThatThrownBy(
                () -> {
                    // when
                    domainStreakService.getCalculateResponse(
                            habit.getId());
                    // then
                })
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessage(ExceptionMessages.entityNotFoundExceptionMessage);
    }

}
