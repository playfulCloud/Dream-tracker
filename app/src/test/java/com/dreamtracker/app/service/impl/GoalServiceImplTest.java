package com.dreamtracker.app.service.impl;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.Mockito.when;

import com.dreamtracker.app.entity.Goal;
import com.dreamtracker.app.entity.User;
import com.dreamtracker.app.exception.EntityNotFoundException;
import com.dreamtracker.app.exception.ExceptionMessages;
import com.dreamtracker.app.fixtures.GoalFixtures;
import com.dreamtracker.app.fixtures.HabitFixture;
import com.dreamtracker.app.fixtures.UserFixtures;
import com.dreamtracker.app.repository.GoalRepository;
import com.dreamtracker.app.repository.HabitRepository;
import com.dreamtracker.app.request.GoalAssignHabitRequest;
import com.dreamtracker.app.response.GoalResponse;
import com.dreamtracker.app.response.Page;
import com.dreamtracker.app.security.CurrentUserProvider;
import com.dreamtracker.app.service.GoalService;
import com.dreamtracker.app.service.UserService;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class GoalServiceImplTest implements UserFixtures, GoalFixtures, HabitFixture {

  private final CurrentUserProvider currentUserProvider = new MockCurrentUserProviderImpl();
  private final GoalRepository goalRepository = Mockito.mock(GoalRepository.class);
  private final UserService userService = Mockito.mock(UserService.class);
  private final HabitRepository habitRepository = Mockito.mock(HabitRepository.class);
  private User sampleUser;
  private GoalService goalService;

  @BeforeEach
  void setUp() {
    sampleUser = getSampleUser(currentUserProvider.getCurrentUser()).build();
    goalService =
        new GoalServiceImpl(goalRepository, userService, currentUserProvider, habitRepository);
  }

  @Test
  void createGoalPositiveTestCase() {
    // given
    var sampleGoal = getSampleGoalBuilder(sampleUser).build();
    var sampleGoalRequest = getSampleGoalRequestBuilder().build();
    when(userService.findById(currentUserProvider.getCurrentUser()))
        .thenReturn(Optional.of(sampleUser));
    when(goalRepository.save(
            Goal.builder()
                .name(sampleGoalRequest.name())
                .duration(sampleGoalRequest.duration())
                .user(sampleUser)
                .habitList(new ArrayList<>())
                .build()))
        .thenReturn(sampleGoal);
    // when
    var actualGoalResponse = goalService.createGoal(sampleGoalRequest);
    var expectedGoalResponse = getExpectedGoalResponse().build();
    // then
    assertThat(actualGoalResponse).isEqualTo(expectedGoalResponse);
  }

  @Test
  void createGoalEntityNotFoundException() {
    // given
    var sampleGoalRequest = getSampleGoalRequestBuilder().build();
    when(userService.findById(currentUserProvider.getCurrentUser())).thenReturn(Optional.empty());
    // when
    assertThatThrownBy(() -> goalService.createGoal(sampleGoalRequest))
        // then
        .hasMessage(ExceptionMessages.entityNotFoundExceptionMessage)
        .isInstanceOf(EntityNotFoundException.class);
  }

  @Test
  void deleteTestPositiveCase() {
    // given
    when(goalRepository.existsById(currentUserProvider.getCurrentUser())).thenReturn(true);
    var expected = true;
    // when
    var actual = goalService.delete(currentUserProvider.getCurrentUser());
    // then
    assertThat(actual).isEqualTo(expected);
  }

  @Test
  void deleteTestNegativeCase() {
    // given
    when(goalRepository.existsById(currentUserProvider.getCurrentUser())).thenReturn(false);
    var expected = false;
    // when
    var actual = goalService.delete(currentUserProvider.getCurrentUser());
    // then
    assertThat(actual).isEqualTo(expected);
  }

  @Test
  void updateGoalPositiveTestCase() {
    // given
    var sampleGoal = getSampleGoalBuilder(sampleUser).build();
    var sampleGoalRequest = getSampleGoalRequestBuilder().build();
    var expectedGoalResponse = getExpectedGoalResponse().build();
    when(goalRepository.findById(sampleGoal.getUuid())).thenReturn(Optional.of(sampleGoal));
    when(goalRepository.save(sampleGoal)).thenReturn(sampleGoal);
    // when
    var actualGoalResponse = goalService.updateGoal(sampleGoal.getUuid(), sampleGoalRequest);
    // then
    assertThat(actualGoalResponse).isEqualTo(expectedGoalResponse);
  }

  @Test
  void updateGoalEntityNotFoundException() {
    // given
    var sampleGoalRequest = getSampleGoalRequestBuilder().build();
    var sampleGoal = getSampleGoalBuilder(sampleUser).build();
    when(goalRepository.findById(sampleGoal.getUuid())).thenReturn(Optional.empty());
    // when
    assertThatThrownBy(() -> goalService.updateGoal(sampleGoal.getUuid(), sampleGoalRequest))
        // then
        .isInstanceOf(EntityNotFoundException.class)
        .hasMessage(ExceptionMessages.entityNotFoundExceptionMessage);
  }

  @Test
  void getAllGoalsPositiveTestCase() {
    // given
    var sampleGoalForPage = getSampleGoalForPageBuilder(sampleUser).build();
    sampleUser.getGoals().add(sampleGoalForPage);
    var expectedPage = new Page<GoalResponse>();
    var expectedPageItems =
        List.of(
            GoalResponse.builder()
                .id(UUID.fromString("336d8a9a-2464-41f2-8a8f-2d37a78c88ae"))
                .name(sampleGoalForPage.getName())
                .duration(sampleGoalForPage.getDuration())
                .build());
    expectedPage.setItems(expectedPageItems);
    when(userService.findById(currentUserProvider.getCurrentUser()))
        .thenReturn(Optional.of(sampleUser));
    // when
    var actualPageResponse = goalService.getAllUserGoals();
    // then
    assertThat(actualPageResponse).isEqualTo(expectedPage);
  }

  @Test
  void getAllUserGoalsEmptyPage() {
    // given
    when(userService.findById(currentUserProvider.getCurrentUser())).thenReturn(Optional.empty());
    // when
    var actualPageResponse = goalService.getAllUserGoals();
    // then
    assertThat(actualPageResponse.getItems().size()).isEqualTo(0);
  }

  @Test
  void associateHabitWithGoalPositiveTestCase() {
    // given
    var sampleHabit = getSampleHabitBuilder(sampleUser).build();
    var sampleGoal = getSampleGoalBuilder(sampleUser).build();

    when(habitRepository.findById(sampleHabit.getId())).thenReturn(Optional.of(sampleHabit));
    when(goalRepository.findById(sampleGoal.getUuid())).thenReturn(Optional.of(sampleGoal));

    // when
    goalService.associateHabitWithGoal(
        sampleGoal.getUuid(), new GoalAssignHabitRequest(sampleHabit.getId(), 25));
    var isGoalAssociated = sampleHabit.getGoals().contains(sampleGoal);
    var isHabitAssociated = sampleGoal.getHabitList().contains(sampleHabit);

    // then
    assertThat(isGoalAssociated).isEqualTo(true);
    assertThat(isHabitAssociated).isEqualTo(true);
  }

  @Test
  void associateHabitWithGoalGoalNotFoundException() {
    // given
    var sampleHabit = getSampleHabitBuilder(sampleUser).build();
    var sampleGoal = getSampleGoalBuilder(sampleUser).build();

    when(habitRepository.findById(sampleHabit.getId())).thenReturn(Optional.of(sampleHabit));
    when(goalRepository.findById(sampleGoal.getUuid())).thenReturn(Optional.empty());

    assertThatThrownBy(
            () ->
                // when
                goalService.associateHabitWithGoal(
                    sampleGoal.getUuid(), new GoalAssignHabitRequest(sampleHabit.getId(), 25))
            // then
            )
        .hasMessage(ExceptionMessages.entityNotFoundExceptionMessage)
        .isInstanceOf(EntityNotFoundException.class);
  }

  @Test
  void associateHabitWithGoalHabitNotFoundException() {
    // given
    var sampleHabit = getSampleHabitBuilder(sampleUser).build();
    var sampleGoal = getSampleGoalBuilder(sampleUser).build();

    when(habitRepository.findById(sampleHabit.getId())).thenReturn(Optional.empty());
    when(goalRepository.findById(sampleGoal.getUuid())).thenReturn(Optional.of(sampleGoal));

    assertThatThrownBy(
            () ->
                // when
                goalService.associateHabitWithGoal(
                    sampleGoal.getUuid(), new GoalAssignHabitRequest(sampleHabit.getId(), 25))
            // then
            )
        .hasMessage(ExceptionMessages.entityNotFoundExceptionMessage)
        .isInstanceOf(EntityNotFoundException.class);
  }

  @Test
  void getGoalByIdPositiveTestCase() {
    // given
    var sampleGoal = getSampleGoalBuilder(sampleUser).build();
    var expectedGoalResponse = getExpectedGoalResponse().build();

    when(goalRepository.findById(sampleGoal.getUuid())).thenReturn(Optional.of(sampleGoal));
    // when
    var actualGoalResponse = goalService.getGoalById(sampleGoal.getUuid());

    // then
    assertThat(actualGoalResponse).isEqualTo(expectedGoalResponse);
  }

  @Test
  void getGoalByIdEntityNotFoundException() {
    // given
    var sampleGoal = getSampleGoalBuilder(sampleUser).build();
    when(goalRepository.findById(sampleGoal.getUuid())).thenReturn(Optional.empty());
    // when
    assertThatThrownBy(() -> goalService.getGoalById(sampleGoal.getUuid()))
        // then
        .isInstanceOf(EntityNotFoundException.class)
        .hasMessage(ExceptionMessages.entityNotFoundExceptionMessage);
  }
}
