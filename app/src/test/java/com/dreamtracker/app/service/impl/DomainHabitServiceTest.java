package com.dreamtracker.app.service.impl;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.Mockito.when;

import com.dreamtracker.app.fixtures.CategoryFixtures;
import com.dreamtracker.app.fixtures.HabitFixture;
import com.dreamtracker.app.fixtures.HabitTrackFixture;
import com.dreamtracker.app.fixtures.UserFixtures;
import com.dreamtracker.app.goal.domain.ports.GoalService;
import com.dreamtracker.app.habit.adapters.api.HabitCategoryCreateRequest;
import com.dreamtracker.app.habit.adapters.api.HabitResponse;
import com.dreamtracker.app.habit.domain.model.HabitTrack;
import com.dreamtracker.app.habit.domain.ports.*;
import com.dreamtracker.app.infrastructure.exception.EntityNotFoundException;
import com.dreamtracker.app.infrastructure.exception.ExceptionMessages;
import com.dreamtracker.app.infrastructure.response.Page;
import com.dreamtracker.app.infrastructure.utils.DateService;
import com.dreamtracker.app.user.config.CurrentUserProvider;
import com.dreamtracker.app.user.config.MockCurrentUserProviderImpl;
import com.dreamtracker.app.user.domain.model.User;
import com.dreamtracker.app.user.domain.ports.UserService;
import com.dreamtracker.app.view.domain.model.aggregate.StatsAggregator;
import java.time.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class DomainHabitServiceTest
    implements HabitFixture, HabitTrackFixture, UserFixtures, CategoryFixtures {

    private final StatsAggregator statsAggregator = Mockito.mock(StatsAggregator.class);
  private final HabitRepositoryPort habitRepositoryPort = Mockito.mock(HabitRepositoryPort.class);
  private final HabitTrackRepositoryPort habitTrackRepositoryPort =
      Mockito.mock(HabitTrackRepositoryPort.class);
  private final UserService userService = Mockito.mock(UserService.class);
  private final CurrentUserProvider currentUserProvider = new MockCurrentUserProviderImpl();
  private final CategoryRepositoryPort categoryRepository = Mockito.mock(CategoryRepositoryPort.class);
  private final GoalService goalService = Mockito.mock(GoalService.class);
  private final HabitTrackService habitTrackService = Mockito.mock(HabitTrackService.class);
  private static final Logger logger = LoggerFactory.getLogger(DomainHabitServiceTest.class);
  private final DateService dateService = Mockito.mock(DateService.class);
  private HabitService habitService;
  private User sampleUser;
  private Clock fixedClock;

  @BeforeEach
  void setUp() {
    fixedClock = Clock.fixed(Instant.now(), ZoneId.systemDefault());
    sampleUser = getSampleUser(currentUserProvider.getCurrentUser()).build();
    habitService =
        new DomainHabitService(
            habitRepositoryPort,
            currentUserProvider,
            categoryRepository,
            habitTrackRepositoryPort,
            statsAggregator,
            goalService,
            habitTrackService,
            fixedClock,dateService);
  }

 @Test
 void createHabitPositiveTestCase(){
    var habitRequest = getSampleHabitRequestBuilder().frequency("DAILY").build();
    var habit =
        getSampleHabitBuilder(currentUserProvider.getCurrentUser())
            .coolDownTill(Instant.now(fixedClock))
            .id(null)
            .build();

    logger.debug(habit.toString());
    logger.debug("habitRequest: {}", habitRequest);

    var expectedHabitResponse =
        getSampleHabitResponseBuilder(currentUserProvider.getCurrentUser())
            .id(habit.getId())
            .cooldownTill(Instant.now(fixedClock).toString())
            .frequency("DAILY")
            .build();
    when(habitRepositoryPort.save(habit)).thenReturn(habit);
    var actualHabitResponse = habitService.createHabit(habitRequest);
    assertThat(expectedHabitResponse).isEqualTo(actualHabitResponse);
 }

  @Test
  void deletePositiveTestCase() {
    // given
    var sampleHabit = getSampleHabitBuilder(sampleUser.getUuid()).build();
    var expectedOutput = true;
    when(habitRepositoryPort.findById(sampleHabit.getId())).thenReturn(Optional.of(sampleHabit));
    // when
    var actualOutput = habitService.delete(sampleHabit.getId());
    // then
    assertThat(actualOutput).isEqualTo(expectedOutput);
  }

  @Test
  void deleteNegativeTestExceptionThrown() {
    // given
    var sampleHabit = getSampleHabitBuilder(sampleUser.getUuid()).build();
    when(habitRepositoryPort.findById(sampleHabit.getId())).thenReturn(Optional.empty());
    // when
    assertThatThrownBy(() -> habitService.delete(sampleHabit.getId()))
        // then
        .isInstanceOf(EntityNotFoundException.class)
        .hasMessage(ExceptionMessages.entityNotFoundExceptionMessage);
  }

  @Test
  void getHabitTrackPositiveTestCase() {
    // given
    var sampleHabit = getSampleHabitBuilder(sampleUser.getUuid()).build();
    var sampleHabitTrack =
        getSampleHabitTrack(
                sampleHabit.getId(),
                Instant.now(fixedClock))
            .build();
    var listOfTracks = List.of(sampleHabitTrack);
    when(habitTrackRepositoryPort.findByHabitUUID(sampleHabit.getId())).thenReturn(listOfTracks);
    // when
    var actualListOfTracks = habitService.getHabitTrack(sampleHabit.getId());
    // then
    assertThat(listOfTracks).isEqualTo(actualListOfTracks);
  }

  @Test
  void getHabitTracksEmptyList() {
    // given
    var sampleHabit = getSampleHabitBuilder(sampleUser.getUuid()).build();
    when(habitTrackRepositoryPort.findByHabitUUID(sampleHabit.getId()))
        .thenReturn(new ArrayList<HabitTrack>());
    // when
    var actualListOfTracks = habitService.getHabitTrack(sampleHabit.getId());
    // then
    assertThat(actualListOfTracks.size()).isEqualTo(0);
  }

  @Test
  void getAllUserHabitsPositiveTestCase() {
    // given
     var habit =
        getSampleHabitBuilder(currentUserProvider.getCurrentUser())
            .coolDownTill(Instant.now(fixedClock))
                .frequency("DAILY")
            .build();
    when(habitRepositoryPort.findByUserUUID(sampleUser.getUuid())).thenReturn(List.of(habit));
    var sampleHabitResponse = getSampleHabitResponseBuilder(sampleUser.getUuid()).categories(new ArrayList<>()).cooldownTill(Instant.now(fixedClock).toString()).frequency("DAILY").build();
    var expectedResponsePageItems = List.of(sampleHabitResponse);
    var expectedResponsePage = new Page<HabitResponse>(expectedResponsePageItems);
    // when
    var actualResponsePage = habitService.getAllUserHabits();
    // then
    assertThat(actualResponsePage).isEqualTo(expectedResponsePage);
  }

  @Test
  void getAllUserHabitsEmptyList() {
    // given
    var sampleHabit = getSampleHabitBuilder(sampleUser.getUuid()).build();
    when(habitRepositoryPort.findByUserUUID(sampleHabit.getUserUUID())).thenReturn(new ArrayList<>());
    // when
    var actualResponsePage = habitService.getAllUserHabits();
    // then
    assertThat(actualResponsePage.getItems().size()).isEqualTo(0);
  }

  @Test
  void updateHabit() {
    // given
    var sampleHabit =
        getSampleHabitBuilder(sampleUser.getUuid()).coolDownTill(Instant.now(fixedClock)).frequency("DAILY").build();
    var sampleUpdateRequest = getSampleHabitRequestUpdateBuilder().build();

    var updatedHabit =
        getSampleUpdatedHabitBuilder(sampleUser.getUuid())
            .coolDownTill(Instant.now(fixedClock))
            .build();
    var expectedHabitResponse = getSampleUpdatedHabitResponseBuilder().cooldownTill(Instant.now(fixedClock).toString()).frequency("DAILY").build();

    logger.debug(sampleHabit.toString());
    logger.debug(updatedHabit.toString());

    when(habitRepositoryPort.findById(sampleHabit.getId())).thenReturn(Optional.of(sampleHabit));
    when(habitRepositoryPort.save(updatedHabit)).thenReturn(updatedHabit);
    // when
    var actualOutput = habitService.updateHabit(sampleHabit.getId(), sampleUpdateRequest);
    // then
    assertThat(expectedHabitResponse).isEqualTo(actualOutput);
  }

  @Test
  void updateHabitEntityNotFoundException() {
    // given
    var sampleHabit = getSampleHabitBuilder(sampleUser.getUuid()).build();
    var sampleUpdateRequest = getSampleHabitRequestUpdateBuilder().build();
    var updatedHabit = getSampleUpdatedHabitBuilder(sampleUser.getUuid()).build();
    when(habitRepositoryPort.findById(sampleHabit.getId())).thenReturn(Optional.empty());
    when(habitRepositoryPort.save(updatedHabit)).thenReturn(updatedHabit);
    // when
    assertThatThrownBy(() -> habitService.updateHabit(sampleHabit.getId(), sampleUpdateRequest))
        // then
        .hasMessage(ExceptionMessages.entityNotFoundExceptionMessage)
        .isInstanceOf(EntityNotFoundException.class);
  }

  @Test
  void linkCategoryWithHabit() {
    // given
    var sampleHabit = getSampleHabitBuilder(sampleUser.getUuid()).build();
    var sampleCategory = getSampleCategoryBuilder(sampleUser.getUuid()).build();
    var habitCategoryRequest =
        HabitCategoryCreateRequest.builder().id(sampleCategory.getId()).build();

    when(habitRepositoryPort.findById(sampleHabit.getId())).thenReturn(Optional.of(sampleHabit));
    when(categoryRepository.findById(habitCategoryRequest.id()))
        .thenReturn(Optional.of(sampleCategory));
    // when
    habitService.linkCategoryWithHabit(sampleHabit.getId(), habitCategoryRequest);
    // then
    assertThat(sampleHabit)
        .isEqualTo(getSampleHabitBuilder(sampleUser.getUuid()).categories(List.of(sampleCategory)).build());
    assertThat(sampleCategory)
        .isEqualTo(getSampleCategoryBuilder(sampleUser.getUuid()).habits(List.of(sampleHabit)).build());
  }

  @Test
  void linkCategoryWithHabitHabitEntityNotFoundException() {
    // given
    var sampleHabit = getSampleHabitBuilder(sampleUser.getUuid()).build();
    var sampleCategory = getSampleCategoryBuilder(sampleUser.getUuid()).build();
    var habitCategoryRequest =
        HabitCategoryCreateRequest.builder().id(sampleCategory.getId()).build();

    when(habitRepositoryPort.findById(sampleHabit.getId())).thenReturn(Optional.empty());
    assertThatThrownBy(
            // when
            () -> habitService.linkCategoryWithHabit(sampleHabit.getId(), habitCategoryRequest))
        // then
        .isInstanceOf(EntityNotFoundException.class)
        .hasMessage(ExceptionMessages.entityNotFoundExceptionMessage);
  }

  @Test
  void linkCategoryWithHabitCategoryEntityNotFoundException() {
    // given
    var sampleHabit = getSampleHabitBuilder(sampleUser.getUuid()).build();
    var sampleCategory = getSampleCategoryBuilder(sampleUser.getUuid()).build();
    var habitCategoryRequest =
        HabitCategoryCreateRequest.builder().id(sampleCategory.getId()).build();
    when(categoryRepository.findById(habitCategoryRequest.id())).thenReturn(Optional.empty());
    assertThatThrownBy(
            // when
            () -> habitService.linkCategoryWithHabit(sampleHabit.getId(), habitCategoryRequest))
        // then
        .isInstanceOf(EntityNotFoundException.class)
        .hasMessage(ExceptionMessages.entityNotFoundExceptionMessage);
  }

  @Test
  void manageHabitsBasedOnCooldownPositiveTest() {
    // given
    var currentInstant = Instant.now(fixedClock);
    var sampleHabit =
        getSampleHabitBuilder(sampleUser.getUuid()).coolDownTill(currentInstant).build();
    var sampleHabitTrack = getSampleHabitTrackRequest(sampleHabit.getId()).status("UNDONE").build();
    var sampleHabitTrackResponse = getSampleHabitTrackResponse(Instant.now(fixedClock)).build();
    var listOfHabits = List.of(sampleHabit);
    when(habitRepositoryPort.findByCoolDownTillAfter(Instant.now(fixedClock)))
        .thenReturn(listOfHabits);
    when(habitTrackService.trackTheHabit(sampleHabitTrack)).thenReturn(sampleHabitTrackResponse);
    when(habitRepositoryPort.save(sampleHabit)).thenReturn(sampleHabit);
    // when
    logger.debug("currentInstant: {}", currentInstant);
    var isCompleted = habitService.manageHabitsBasedOnCooldown();
    // then
    assertThat(isCompleted).isTrue();
  }
}
