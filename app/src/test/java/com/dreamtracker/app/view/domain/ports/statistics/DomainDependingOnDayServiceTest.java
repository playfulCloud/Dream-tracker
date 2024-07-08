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
import com.dreamtracker.app.view.adapters.api.DependingOnDayComponentResponse;
import com.dreamtracker.app.view.domain.model.aggregate.AggregatesFixtures;
import com.dreamtracker.app.view.domain.model.aggregate.DependingOnDayAggregate;
import com.dreamtracker.app.view.domain.ports.DependingOnDayRepositoryPort;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Stream;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mockito;

class DomainDependingOnDayServiceTest
    implements HabitFixture, AggregatesFixtures, HabitTrackFixture {

  private final DependingOnDayRepositoryPort dependingOnDayRepositoryPort =
      Mockito.mock(DependingOnDayRepositoryPort.class);
  private final CurrentUserProvider currentUserProvider = new MockCurrentUserProviderImpl();
  private DomainDependingOnDayService domainDependingOnDayService;
  private final Habit habit = getSampleHabitBuilder(currentUserProvider.getCurrentUser()).build();
  private final DateService dateService = new DateService();
  private static final UUID habitUUID = UUID.randomUUID();
  private static final String MONDAY_DATE = "2024-07-01T00:00:00Z";
  private static final String TUESDAY_DATE = "2024-07-02T00:00:00Z";
  private static final String WEDNESDAY_DATE = "2024-07-03T00:00:00Z";
  private static final String THURSDAY_DATE = "2024-07-04T00:00:00Z";
  private static final String FRIDAY_DATE = "2024-07-05T00:00:00Z";
  private static final String SATURDAY_DATE = "2024-07-06T00:00:00Z";
  private static final String SUNDAY_DATE = "2024-07-07T00:00:00Z";

  @BeforeEach
  void setUp() {
    domainDependingOnDayService = new DomainDependingOnDayService(dependingOnDayRepositoryPort);
  }

  @Test
  void initializeAggregatesPositiveTestCase() {
    // given
    var aggregate = getDependingOnDayAggregateBuilder(habit.getId()).id(null).build();
    var aggregateSavedToDB = getDependingOnDayAggregateBuilder(habit.getId()).build();
    var expectedComponentResponse = getDependingOnDayStatsComponentResponse().build();

    when(dependingOnDayRepositoryPort.save(aggregate)).thenReturn(aggregateSavedToDB);
    // when
    var actualComponentResponse = domainDependingOnDayService.initializeAggregates(habit.getId());
    // then
    assertThat(actualComponentResponse).isEqualTo(expectedComponentResponse);
  }

    private static Stream<Arguments>increaseSuccessRateArguments () {
        return Stream.of(
                Arguments.of(MONDAY_DATE,getAggregateWithSpecifiedCountsBasedOnDay(MONDAY_DATE,0,0), getResponseComponentByDay(100,MONDAY_DATE)),
                Arguments.of(TUESDAY_DATE,getAggregateWithSpecifiedCountsBasedOnDay(TUESDAY_DATE,0,0), getResponseComponentByDay(100,TUESDAY_DATE)),
                Arguments.of(WEDNESDAY_DATE,getAggregateWithSpecifiedCountsBasedOnDay(WEDNESDAY_DATE,0,0), getResponseComponentByDay(100,WEDNESDAY_DATE)),
                Arguments.of(THURSDAY_DATE,getAggregateWithSpecifiedCountsBasedOnDay(THURSDAY_DATE,0,0), getResponseComponentByDay(100,THURSDAY_DATE)),
                Arguments.of(FRIDAY_DATE,getAggregateWithSpecifiedCountsBasedOnDay(FRIDAY_DATE,0,0), getResponseComponentByDay(100,FRIDAY_DATE)),
                Arguments.of(SATURDAY_DATE,getAggregateWithSpecifiedCountsBasedOnDay(SATURDAY_DATE,0,0), getResponseComponentByDay(100,SATURDAY_DATE)),
                Arguments.of(SUNDAY_DATE,getAggregateWithSpecifiedCountsBasedOnDay(SUNDAY_DATE,0,0), getResponseComponentByDay(100,SUNDAY_DATE))
        );
    }

  private static Stream<Arguments> increaseSuccessRateWithUnDoneValueArguments() {
    return Stream.of(
        Arguments.of(
            MONDAY_DATE,
            getAggregateWithSpecifiedCountsBasedOnDay(MONDAY_DATE, 0, 3),
            getResponseComponentByDay(25, MONDAY_DATE)),
        Arguments.of(
            TUESDAY_DATE,
            getAggregateWithSpecifiedCountsBasedOnDay(TUESDAY_DATE, 0, 3),
            getResponseComponentByDay(25, TUESDAY_DATE)),
        Arguments.of(
            WEDNESDAY_DATE,
            getAggregateWithSpecifiedCountsBasedOnDay(WEDNESDAY_DATE, 0, 3),
            getResponseComponentByDay(25, WEDNESDAY_DATE)),
        Arguments.of(
            THURSDAY_DATE,
            getAggregateWithSpecifiedCountsBasedOnDay(THURSDAY_DATE, 0, 3),
            getResponseComponentByDay(25, THURSDAY_DATE)),
        Arguments.of(
            FRIDAY_DATE,
            getAggregateWithSpecifiedCountsBasedOnDay(FRIDAY_DATE, 0, 3),
            getResponseComponentByDay(25, FRIDAY_DATE)),
        Arguments.of(
            SATURDAY_DATE,
            getAggregateWithSpecifiedCountsBasedOnDay(SATURDAY_DATE, 0, 3),
            getResponseComponentByDay(25, SATURDAY_DATE)),
        Arguments.of(
            SUNDAY_DATE,
            getAggregateWithSpecifiedCountsBasedOnDay(SUNDAY_DATE, 0, 3),
            getResponseComponentByDay(25, SUNDAY_DATE)));
  }

    private static Stream<Arguments>decreaseSuccessRateArguments () {
    return Stream.of(
        Arguments.of(
            MONDAY_DATE,
            getAggregateWithSpecifiedCountsBasedOnDay(MONDAY_DATE, 1, 0),
            getResponseComponentByDay(50, MONDAY_DATE)),
        Arguments.of(
            TUESDAY_DATE,
            getAggregateWithSpecifiedCountsBasedOnDay(TUESDAY_DATE, 1, 0),
            getResponseComponentByDay(50, TUESDAY_DATE)),
        Arguments.of(
            WEDNESDAY_DATE,
            getAggregateWithSpecifiedCountsBasedOnDay(WEDNESDAY_DATE, 1, 0),
            getResponseComponentByDay(50, WEDNESDAY_DATE)),
        Arguments.of(
            THURSDAY_DATE,
            getAggregateWithSpecifiedCountsBasedOnDay(THURSDAY_DATE, 1, 0),
            getResponseComponentByDay(50, THURSDAY_DATE)),
        Arguments.of(
            FRIDAY_DATE,
            getAggregateWithSpecifiedCountsBasedOnDay(FRIDAY_DATE, 1, 0),
            getResponseComponentByDay(50, FRIDAY_DATE)),
        Arguments.of(
            SATURDAY_DATE,
            getAggregateWithSpecifiedCountsBasedOnDay(SATURDAY_DATE, 1, 0),
            getResponseComponentByDay(50, SATURDAY_DATE)),
        Arguments.of(
            SUNDAY_DATE,
            getAggregateWithSpecifiedCountsBasedOnDay(SUNDAY_DATE, 1, 0),
            getResponseComponentByDay(50, SUNDAY_DATE)));
    }

  private static Stream<Arguments> noChangeOfSuccessRateArguments() {
    return Stream.of(
        Arguments.of(
            MONDAY_DATE,
            getAggregateWithSpecifiedCountsBasedOnDay(MONDAY_DATE, 0, 0),
            getResponseComponentByDay(0, MONDAY_DATE)),
        Arguments.of(
            TUESDAY_DATE,
            getAggregateWithSpecifiedCountsBasedOnDay(TUESDAY_DATE, 0, 0),
            getResponseComponentByDay(0, TUESDAY_DATE)),
        Arguments.of(
            WEDNESDAY_DATE,
            getAggregateWithSpecifiedCountsBasedOnDay(WEDNESDAY_DATE, 0, 0),
            getResponseComponentByDay(0, WEDNESDAY_DATE)),
        Arguments.of(
            THURSDAY_DATE,
            getAggregateWithSpecifiedCountsBasedOnDay(THURSDAY_DATE, 0, 0),
            getResponseComponentByDay(0, THURSDAY_DATE)),
        Arguments.of(
            FRIDAY_DATE,
            getAggregateWithSpecifiedCountsBasedOnDay(FRIDAY_DATE, 0, 0),
            getResponseComponentByDay(0, FRIDAY_DATE)),
        Arguments.of(
            SATURDAY_DATE,
            getAggregateWithSpecifiedCountsBasedOnDay(SATURDAY_DATE, 0, 0),
            getResponseComponentByDay(0, SATURDAY_DATE)),
        Arguments.of(
            SUNDAY_DATE,
            getAggregateWithSpecifiedCountsBasedOnDay(SUNDAY_DATE, 0, 0),
            getResponseComponentByDay(0, SUNDAY_DATE)));
  }

  private static Stream<Arguments> undoneArguments() {
    return Stream.concat(noChangeOfSuccessRateArguments(), decreaseSuccessRateArguments());
  }

  private static Stream<Arguments> doneArguments() {
    return Stream.concat(
        increaseSuccessRateArguments(), increaseSuccessRateWithUnDoneValueArguments());
  }

    private static DependingOnDayComponentResponse getResponseComponentByDay(int successRateOfDay, String date){
      DependingOnDayComponentResponse responseBasedOnDay = null;
      switch(date){
            case MONDAY_DATE:
                responseBasedOnDay = DependingOnDayComponentResponse.builder().mondayRateSuccessRate(successRateOfDay).build();
                break;
          case TUESDAY_DATE:
              responseBasedOnDay = DependingOnDayComponentResponse.builder().tuesdayRateSuccessRate(successRateOfDay).build();
          break;
          case WEDNESDAY_DATE:
              responseBasedOnDay = DependingOnDayComponentResponse.builder().wednesdayRateSuccessRate(successRateOfDay).build();
          break;
          case THURSDAY_DATE:
              responseBasedOnDay = DependingOnDayComponentResponse.builder().thursdayRateSuccessRate(successRateOfDay).build();
          break;
          case FRIDAY_DATE:
              responseBasedOnDay = DependingOnDayComponentResponse.builder().fridayRateSuccessRate(successRateOfDay).build();
          break;
          case SATURDAY_DATE:
              responseBasedOnDay = DependingOnDayComponentResponse.builder().saturdayRateSuccessRate(successRateOfDay).build();
          break;
          case SUNDAY_DATE:
              responseBasedOnDay = DependingOnDayComponentResponse.builder().sundayRateSuccessRate(successRateOfDay).build();
          break;
        }
       return responseBasedOnDay;
    }

    private static DependingOnDayAggregate getAggregateWithSpecifiedCountsBasedOnDay(String date, int doneCount, int undoneCount){
      DependingOnDayAggregate dependingOnDayAggregate = null;
        switch(date){
            case MONDAY_DATE:
                dependingOnDayAggregate = DependingOnDayAggregate.builder().habitUUID(habitUUID).mondayDoneCount(doneCount).mondayUnDoneCount(undoneCount).build();
                break;
            case TUESDAY_DATE:
        dependingOnDayAggregate =
            DependingOnDayAggregate.builder()
                .habitUUID(habitUUID)
                .tuesdayDoneCount(doneCount)
                .tuesdayUnDoneCount(undoneCount)
                .build();
                break;
            case WEDNESDAY_DATE:
                dependingOnDayAggregate = DependingOnDayAggregate.builder().habitUUID(habitUUID).wednesdayDoneCount(doneCount).wednesdayUnDoneCount(undoneCount).build();
                break;
            case THURSDAY_DATE:
        dependingOnDayAggregate =
            DependingOnDayAggregate.builder()
                .habitUUID(habitUUID)
                .thursdayDoneCount(doneCount)
                .thursdayUnDoneCount(undoneCount)
                .build();
                break;
            case FRIDAY_DATE:
                dependingOnDayAggregate = DependingOnDayAggregate.builder().habitUUID(habitUUID).fridayDoneCount(doneCount).fridayUnDoneCount(undoneCount).build();
                break;
            case SATURDAY_DATE:
                dependingOnDayAggregate = DependingOnDayAggregate.builder().habitUUID(habitUUID).saturdayDoneCount(doneCount).saturdayUnDoneCount(undoneCount).build();
                break;
            case SUNDAY_DATE:
                dependingOnDayAggregate = DependingOnDayAggregate.builder().habitUUID(habitUUID).sundayDoneCount(doneCount).sundayUnDoneCount(undoneCount).build();
                break;
        }
        return dependingOnDayAggregate;
    }

  @ParameterizedTest
  @MethodSource("doneArguments")
  void updateAggregatesAndCalculateResponsePositiveTestCaseDone(
      String date,
      DependingOnDayAggregate dependingOnDayAggregate,
      DependingOnDayComponentResponse expected) {
        // given
        var habitTrackResponse = getSampleHabitTrackResponse(date).status(HabitTrackStatus.DONE.toString()).build();

        when(dependingOnDayRepositoryPort.findByHabitUUID(habit.getId()))
                .thenReturn(Optional.of(dependingOnDayAggregate));
        when(dependingOnDayRepositoryPort.save(dependingOnDayAggregate)).thenReturn(dependingOnDayAggregate);

        // when
        var actual = domainDependingOnDayService.updateAggregatesAndCalculateResponse(habit.getId(), habitTrackResponse);

        // then
        assertThat(actual).isEqualTo(expected);
    }

  @ParameterizedTest
  @MethodSource("undoneArguments")
  void updateAggregatesAndCalculateResponsePositiveTestCaseUndone(
      String date,
      DependingOnDayAggregate dependingOnDayAggregate,
      DependingOnDayComponentResponse expected) {
        // given
        var habitTrackResponse = getSampleHabitTrackResponse(date).status(HabitTrackStatus.UNDONE.toString()).build();

        when(dependingOnDayRepositoryPort.findByHabitUUID(habit.getId()))
                .thenReturn(Optional.of(dependingOnDayAggregate));
        when(dependingOnDayRepositoryPort.save(dependingOnDayAggregate)).thenReturn(dependingOnDayAggregate);

        // when
        var actual = domainDependingOnDayService.updateAggregatesAndCalculateResponse(habit.getId(), habitTrackResponse);

        // then
        assertThat(actual).isEqualTo(expected);
    }

  @Test
  void getCalculateResponsePositiveTestCase() {
   // given
    var dependingOnDayAggregate = getDependingOnDayAggregateBuilder(habit.getId()).build();

    when(dependingOnDayRepositoryPort.findByHabitUUID(habit.getId()))
        .thenReturn(Optional.of(dependingOnDayAggregate));

    var expectedResponse = getDependingOnDayStatsComponentResponse().build();


    // when
    var actual = domainDependingOnDayService.getCalculateResponse(habit.getId());

    // then
    assertThat(expectedResponse).isEqualTo(actual);

  }
    @Test
    void updateAggregatesAndCalculateResponseEntityNotFoundException() {
        // given
        var dependingOnDayAggregate = getDependingOnDayAggregateBuilder(habit.getId()).build();

        var habitTrackResponse = getSampleHabitTrackResponse(dateService.getCurrentDateInISO8601()).status(HabitTrackStatus.UNDONE.toString()).build();

        when(dependingOnDayRepositoryPort.findByHabitUUID(habit.getId()))
                .thenReturn(Optional.empty());


        assertThatThrownBy(() -> {
            // when
            domainDependingOnDayService.updateAggregatesAndCalculateResponse(habit.getId(),habitTrackResponse);
            // then
        }).isInstanceOf(EntityNotFoundException.class).hasMessage(ExceptionMessages.entityNotFoundExceptionMessage);


    }

    @Test
    void getCalculateResponseEntityNotFoundException() {
        // given
        var dependingOnDayAggregate = getDependingOnDayAggregateBuilder(habit.getId()).build();

        when(dependingOnDayRepositoryPort.findByHabitUUID(habit.getId()))
                .thenReturn(Optional.empty());


        assertThatThrownBy(() -> {
            // when
             domainDependingOnDayService.getCalculateResponse(habit.getId());
            // then
        }).isInstanceOf(EntityNotFoundException.class).hasMessage(ExceptionMessages.entityNotFoundExceptionMessage);


    }
}