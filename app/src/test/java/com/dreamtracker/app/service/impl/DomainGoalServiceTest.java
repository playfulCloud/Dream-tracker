package com.dreamtracker.app.service.impl;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.Mockito.when;

import com.dreamtracker.app.fixtures.GoalFixtures;
import com.dreamtracker.app.fixtures.HabitFixture;
import com.dreamtracker.app.fixtures.UserFixtures;
import com.dreamtracker.app.goal.adapters.api.GoalResponse;
import com.dreamtracker.app.goal.domain.model.Goal;
import com.dreamtracker.app.goal.domain.model.GoalStatus;
import com.dreamtracker.app.goal.domain.ports.DomainGoalService;
import com.dreamtracker.app.goal.domain.ports.GoalRepositoryPort;
import com.dreamtracker.app.goal.domain.ports.GoalService;
import com.dreamtracker.app.habit.domain.ports.HabitRepositoryPort;
import com.dreamtracker.app.infrastructure.exception.EntityNotFoundException;
import com.dreamtracker.app.infrastructure.exception.ExceptionMessages;
import com.dreamtracker.app.infrastructure.repository.SpringDataUserRepository;
import com.dreamtracker.app.infrastructure.response.Page;
import com.dreamtracker.app.user.config.CurrentUserProvider;
import com.dreamtracker.app.user.config.MockCurrentUserProviderImpl;
import com.dreamtracker.app.user.domain.model.User;

import java.time.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.platform.commons.logging.Logger;
import org.junit.platform.commons.logging.LoggerFactory;
import org.mockito.Mockito;

class DomainGoalServiceTest implements UserFixtures, GoalFixtures, HabitFixture {

    private final CurrentUserProvider currentUserProvider = new MockCurrentUserProviderImpl();
    private final GoalRepositoryPort goalRepositoryPort = Mockito.mock(GoalRepositoryPort.class);
    private final SpringDataUserRepository springDataUserRepository = Mockito.mock(SpringDataUserRepository.class);
    private final HabitRepositoryPort habitRepositoryPort = Mockito.mock(HabitRepositoryPort.class);
    private Clock clock;
    private static final Logger logger = LoggerFactory.getLogger(DomainGoalServiceTest.class);
    private User sampleUser;
    private GoalService goalService;

    @BeforeEach
    void setUp() {
        sampleUser = getSampleUser(currentUserProvider.getCurrentUser()).build();
        clock = Clock.fixed(Instant.parse("2024-09-17T00:00:00Z"), ZoneOffset.UTC);
        goalService =
                new DomainGoalService(goalRepositoryPort, springDataUserRepository, currentUserProvider, habitRepositoryPort, clock);
    }

    @Test
    void createGoalPositiveTestCase() {
        // given
        var sampleHabit = getSampleHabitBuilder(currentUserProvider.getCurrentUser()).build();
        var sampleGoalRequest = getSampleGoalRequestBuilder().habitID(sampleHabit.getId()).completionCount(1).build();

        var sampleGoal =
                getSampleGoalBuilder(currentUserProvider.getCurrentUser())
                        .completionCount(sampleGoalRequest.completionCount())
                        .habitUUID(sampleGoalRequest.habitID())
                        .status(GoalStatus.ACTIVE.toString())
                        .createdAt(Instant.now(clock))
                        .currentCount(0)
                        .build();

        when(springDataUserRepository.findById(currentUserProvider.getCurrentUser()))
                .thenReturn(Optional.of(sampleUser));

        when(habitRepositoryPort.findById(sampleGoalRequest.habitID()))
                .thenReturn(Optional.of(sampleHabit));
        when(goalRepositoryPort.save(
                Goal.builder()
                        .name(sampleGoalRequest.name())
                        .duration(sampleGoalRequest.duration())
                        .userUUID(sampleUser.getUuid())
                        .habitUUID(sampleGoalRequest.habitID())
                        .completionCount(sampleGoalRequest.completionCount())
                        .status(GoalStatus.ACTIVE.toString())
                        .currentCount(0)
                        .build()))
                .thenReturn(sampleGoal);
        // when
        var actualGoalResponse = goalService.createGoal(sampleGoalRequest);
        var expectedGoalResponse =
                getExpectedGoalResponse()
                        .completionCount(sampleGoalRequest.completionCount())
                        .habitID(sampleGoalRequest.habitID())
                        .status(GoalStatus.ACTIVE.toString())
                        .createdAt(String.valueOf(Instant.now(clock)))
                        .build();
        // then
        assertThat(actualGoalResponse).isEqualTo(expectedGoalResponse);
    }

    @Test
    void createGoalEntityNotFoundExceptionThrown() {
        // given
        var sampleHabit = getSampleHabitBuilder(currentUserProvider.getCurrentUser()).build();
        var sampleGoalRequest = getSampleGoalRequestBuilder().habitID(sampleHabit.getId()).build();

        var sampleGoal =
                getSampleGoalBuilder(currentUserProvider.getCurrentUser())
                        .completionCount(sampleGoalRequest.completionCount())
                        .habitUUID(sampleGoalRequest.habitID())
                        .build();
        when(springDataUserRepository.findById(currentUserProvider.getCurrentUser()))
                .thenReturn(Optional.of(sampleUser));

        when(habitRepositoryPort.findById(sampleGoalRequest.habitID())).thenReturn(Optional.empty());
        when(goalRepositoryPort.save(
                Goal.builder()
                        .name(sampleGoalRequest.name())
                        .duration(sampleGoalRequest.duration())
                        .userUUID(sampleUser.getUuid())
                        .habitUUID(sampleGoalRequest.habitID())
                        .completionCount(sampleGoalRequest.completionCount())
                        .build()))
                .thenReturn(sampleGoal);
        assertThatThrownBy(
                () -> {
                    // when
                    goalService.createGoal(sampleGoalRequest);
                    // then
                })
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessage(ExceptionMessages.entityNotFoundExceptionMessage);
    }

    @Test
    void deleteTestPositiveCase() {
        // given
        var sampleHabit = getSampleHabitBuilder(currentUserProvider.getCurrentUser()).build();
        var sampleGoal = getSampleGoalBuilder(currentUserProvider.getCurrentUser()).habitUUID(sampleHabit.getId()).build();
        when(goalRepositoryPort.findById(sampleGoal.getUuid())).thenReturn(Optional.of(sampleGoal));
        when(habitRepositoryPort.findById(sampleHabit.getId())).thenReturn(Optional.of(sampleHabit));
        var expected = true;
        // when
        var actual = goalService.delete(sampleGoal.getUuid());
        // then
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void deleteTestCaseEntityNotFoundExceptionThrownHabit() {
        // given
        var sampleHabit = getSampleHabitBuilder(currentUserProvider.getCurrentUser()).build();
        var sampleGoal = getSampleGoalBuilder(currentUserProvider.getCurrentUser()).habitUUID(sampleHabit.getId()).build();
        when(goalRepositoryPort.findById(sampleGoal.getUuid())).thenReturn(Optional.of(sampleGoal));
        when(habitRepositoryPort.findById(sampleHabit.getId())).thenReturn(Optional.empty());
        //when
        assertThatThrownBy(() -> {
            goalService.delete(sampleGoal.getUuid());
            //then
        }).isInstanceOf(EntityNotFoundException.class).hasMessage(ExceptionMessages.entityNotFoundExceptionMessage);
    }

    @Test
    void deleteTestCaseEntityNotFoundExceptionThrownGoal() {
        // given
        var sampleHabit = getSampleHabitBuilder(currentUserProvider.getCurrentUser()).build();
        var sampleGoal = getSampleGoalBuilder(currentUserProvider.getCurrentUser()).habitUUID(sampleHabit.getId()).build();
        when(goalRepositoryPort.findById(sampleGoal.getUuid())).thenReturn(Optional.empty());
        when(habitRepositoryPort.findById(sampleHabit.getId())).thenReturn(Optional.of(sampleHabit));
        //when
        assertThatThrownBy(() -> {
            goalService.delete(sampleGoal.getUuid());
            //then
        }).isInstanceOf(EntityNotFoundException.class).hasMessage(ExceptionMessages.entityNotFoundExceptionMessage);
    }

    @Test
    void updateGoalPositiveTestCase() {
        // given
        var sampleGoal = getSampleGoalBuilder(currentUserProvider.getCurrentUser()).createdAt(Instant.now(clock)).build();
        var sampleGoalRequest = getSampleGoalRequestBuilder().build();
        var expectedGoalResponse = getUpdatedExpectedGoalResponse().createdAt(String.valueOf(Instant.now(clock))).build();
        var updatedGoal = getSampleUpdatedGoalBuilder(currentUserProvider.getCurrentUser()).createdAt(Instant.now(clock)).build();
        when(goalRepositoryPort.findById(sampleGoal.getUuid())).thenReturn(Optional.of(sampleGoal));
        when(goalRepositoryPort.save(sampleGoal)).thenReturn(updatedGoal);
        // when
        var actualGoalResponse = goalService.updateGoal(sampleGoal.getUuid(), sampleGoalRequest);
        // then
        assertThat(actualGoalResponse).isEqualTo(expectedGoalResponse);
    }

    @Test
    void updateGoalEntityNotFoundException() {
        // given
        var sampleGoalRequest = getSampleUpdateGoalRequestBuilder().build();
        var sampleGoal = getSampleGoalBuilder(currentUserProvider.getCurrentUser()).build();
        when(goalRepositoryPort.findById(sampleGoal.getUuid())).thenReturn(Optional.empty());
        // when
        assertThatThrownBy(() -> goalService.updateGoal(sampleGoal.getUuid(), sampleGoalRequest))
                // then
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessage(ExceptionMessages.entityNotFoundExceptionMessage);
    }

    @Test
    void getAllGoalsPositiveTestCase() {
        // given
        var sampleGoalForPage = getSampleGoalForPageBuilder(currentUserProvider.getCurrentUser()).createdAt(Instant.now(clock)).build();
        var expectedPageItems =
                List.of(
                        GoalResponse.builder()
                                .id(UUID.fromString("336d8a9a-2464-41f2-8a8f-2d37a78c88ae"))
                                .name(sampleGoalForPage.getName())
                                .duration(sampleGoalForPage.getDuration())
                                .createdAt(String.valueOf(Instant.now(clock)))
                                .build());
        var expectedPage = new Page<GoalResponse>(expectedPageItems);
        when(springDataUserRepository.findById(currentUserProvider.getCurrentUser()))
                .thenReturn(Optional.of(sampleUser));
        when(goalRepositoryPort.findByUserUUID(currentUserProvider.getCurrentUser())).thenReturn(List.of(sampleGoalForPage));
        // when
        var actualPageResponse = goalService.getAllUserGoals();
        // then
        assertThat(actualPageResponse).isEqualTo(expectedPage);
    }

    @Test
    void getAllUserGoalsEmptyPage() {
        // given
        when(goalRepositoryPort.findByUserUUID(currentUserProvider.getCurrentUser())).thenReturn(new ArrayList<>());
        // when
        var actualPageResponse = goalService.getAllUserGoals();
        // then
        assertThat(actualPageResponse.getItems().size()).isEqualTo(0);
    }

    @Test
    void getGoalByIdPositiveTestCase() {
        // given
        var sampleGoal = getSampleGoalBuilder(currentUserProvider.getCurrentUser()).createdAt(Instant.now(clock)).build();
        var expectedGoalResponse =
                getExpectedGoalResponse().completionCount(10).habitID(sampleGoal.getHabitUUID()).createdAt(String.valueOf(Instant.now(clock))).build();

        when(goalRepositoryPort.findById(sampleGoal.getUuid())).thenReturn(Optional.of(sampleGoal));
        // when
        var actualGoalResponse = goalService.getGoalById(sampleGoal.getUuid());

        // then
        assertThat(actualGoalResponse).isEqualTo(expectedGoalResponse);
    }

    @Test
    void getGoalByIdEntityNotFoundException() {
        // given
        var sampleGoal = getSampleGoalBuilder(currentUserProvider.getCurrentUser()).build();
        when(goalRepositoryPort.findById(sampleGoal.getUuid())).thenReturn(Optional.empty());
        // when
        assertThatThrownBy(() -> goalService.getGoalById(sampleGoal.getUuid()))
                // then
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessage(ExceptionMessages.entityNotFoundExceptionMessage);
    }

    @Test
    void increaseCompletionCountPositiveTestCase() {
        // given
        var sampleGoal = getSampleGoalBuilder(currentUserProvider.getCurrentUser()).createdAt(Instant.now(clock)).build();
        when(goalRepositoryPort.findById(sampleGoal.getUuid())).thenReturn(Optional.of(sampleGoal));

        when(goalRepositoryPort.save(sampleGoal)).thenReturn(sampleGoal);

        // when
        var actual = goalService.increaseCompletionCount(sampleGoal.getUuid());
        var expectedGoalResponse =
                getExpectedGoalResponse()
                        .completionCount(sampleGoal.getCompletionCount())
                        .currentCount(1)
                        .habitID(sampleGoal.getHabitUUID())
                        .createdAt(String.valueOf(Instant.now(clock)))
                        .build();
        assertThat(expectedGoalResponse).isEqualTo(actual);
    }


    @Test
    void increaseCompletionCountPositiveTestCaseGoalCompleted() {
        // given
        var sampleGoal = getSampleGoalBuilder(currentUserProvider.getCurrentUser()).completionCount(1).createdAt(Instant.now(clock)).build();

        var sampleHabit = getSampleHabitBuilder(currentUserProvider.getCurrentUser()).build();
        when(goalRepositoryPort.findById(sampleGoal.getUuid())).thenReturn(Optional.of(sampleGoal));
        when(habitRepositoryPort.findById(sampleHabit.getId())).thenReturn(Optional.of(sampleHabit));
        when(goalRepositoryPort.save(sampleGoal)).thenReturn(sampleGoal);

        // when
        var actual = goalService.increaseCompletionCount(sampleGoal.getUuid());
        var expectedGoalResponse =
                getExpectedGoalResponse()
                        .completionCount(sampleGoal.getCompletionCount())
                        .currentCount(1)
                        .habitID(sampleGoal.getHabitUUID())
                        .status(GoalStatus.DONE.toString())
                        .createdAt(String.valueOf(Instant.now(clock)))
                        .build();
        assertThat(expectedGoalResponse).isEqualTo(actual);
    }

    @Test
    void increaseCompletionCountEntityNotFoundExceptionThrown() {
        // given
        var sampleGoal = getSampleGoalBuilder(currentUserProvider.getCurrentUser()).build();

        when(goalRepositoryPort.findById(sampleGoal.getUuid())).thenReturn(Optional.empty());

        when(goalRepositoryPort.save(sampleGoal)).thenReturn(sampleGoal);

        // when
        assertThatThrownBy(
                () -> {
                    goalService.increaseCompletionCount(sampleGoal.getUuid());
                    // then
                })
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessage(ExceptionMessages.entityNotFoundExceptionMessage);
    }

    @Test
    void setStatusBasedOnDatePositiveTestCase() {
        // given
        Instant now = Instant.now();

        ZoneId systemZone = ZoneId.systemDefault();
        ZonedDateTime zonedDateTime = now.atZone(systemZone);
        ZonedDateTime yesterdayZonedDateTime = zonedDateTime.plusDays(2);
        Instant yesterdayInstant = yesterdayZonedDateTime.toInstant();

        var expectedGoal =
                getSampleGoalBuilder(currentUserProvider.getCurrentUser())
                        .createdAt(yesterdayInstant)
                        .status(GoalStatus.ACTIVE.toString())
                        .duration("P1D")
                        .version(1)
                        .build();
        var goal =
                getSampleGoalBuilder(currentUserProvider.getCurrentUser())
                        .createdAt(yesterdayInstant)
                        .duration("P1D")
                        .status(GoalStatus.ACTIVE.toString())
                        .createdAt(Instant.parse("2024-09-18T00:00:00Z"))
                        .version(1)
                        .build();
        when(goalRepositoryPort.save(goal)).thenReturn(expectedGoal);
        // when
        var actualGoal = goalService.setStatusBasedOnDate(goal);
        // then
        assertThat(actualGoal).isEqualTo(expectedGoal);
    }

    @Test
    void setStatusBasedOnDateStatusStaysActive() {
        // given
        Instant now = Instant.now();

        ZoneId systemZone = ZoneId.systemDefault();
        ZonedDateTime zonedDateTime = now.atZone(systemZone);
        ZonedDateTime yesterdayZonedDateTime = zonedDateTime.plusDays(2);
        Instant yesterdayInstant = yesterdayZonedDateTime.toInstant();

        var expectedGoal =
                getSampleGoalBuilder(currentUserProvider.getCurrentUser())
                        .createdAt(yesterdayInstant)
                        .status(GoalStatus.FAILED.toString())
                        .duration("P1D")
                        .version(1)
                        .build();
        var goal =
                getSampleGoalBuilder(currentUserProvider.getCurrentUser())
                        .createdAt(yesterdayInstant)
                        .duration("P1D")
                        .status(GoalStatus.ACTIVE.toString())
                        .createdAt(Instant.parse("2024-09-15T00:00:00Z"))
                        .version(1)
                        .build();
        when(goalRepositoryPort.save(goal)).thenReturn(expectedGoal);
        // when
        var actualGoal = goalService.setStatusBasedOnDate(goal);

        // then
        assertThat(actualGoal).isEqualTo(expectedGoal);
    }

    @Test
    void markAsFailedIfNotCompletedPositiveTestCase() {
        // given
        var goal = getSampleGoalBuilder(currentUserProvider.getCurrentUser()).duration("P1D").createdAt(Instant.parse("2024-09-15T00:00:00Z")).build();
        var listOfGoals = List.of(goal);
        when(goalRepositoryPort.findAll()).thenReturn(listOfGoals);
        when(goalRepositoryPort.save(goal)).thenReturn(goal);
        // when
        goalService.markGoalAsFailedIfNotCompleted();

        // then
        assertThat(goalService.markGoalAsFailedIfNotCompleted()).isTrue();
    }
}
