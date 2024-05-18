package com.dreamtracker.app.service.impl;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.Mockito.when;

import com.dreamtracker.app.habit.domain.model.HabitTrack;
import com.dreamtracker.app.user.config.MockCurrentUserProviderImpl;
import com.dreamtracker.app.user.domain.model.User;
import com.dreamtracker.app.habit.domain.ports.HabitServiceImpl;
import com.dreamtracker.app.infrastructure.exception.EntityNotFoundException;
import com.dreamtracker.app.infrastructure.exception.ExceptionMessages;
import com.dreamtracker.app.habit.domain.fixtures.CategoryFixtures;
import com.dreamtracker.app.habit.domain.fixtures.HabitFixture;
import com.dreamtracker.app.habit.domain.fixtures.HabitTrackFixture;
import com.dreamtracker.app.habit.domain.fixtures.UserFixtures;
import com.dreamtracker.app.habit.domain.ports.CategoryRepository;
import com.dreamtracker.app.habit.domain.ports.HabitRepository;
import com.dreamtracker.app.habit.domain.ports.HabitTrackRepository;
import com.dreamtracker.app.habit.adapters.api.HabitCategoryCreateRequest;
import com.dreamtracker.app.habit.adapters.api.HabitResponse;
import com.dreamtracker.app.infrastructure.response.Page;
import com.dreamtracker.app.user.config.CurrentUserProvider;
import com.dreamtracker.app.habit.domain.ports.HabitService;
import com.dreamtracker.app.user.domain.ports.UserService;
import java.time.Clock;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class HabitServiceImplTest
    implements HabitFixture, HabitTrackFixture, UserFixtures, CategoryFixtures {

  private final HabitRepository habitRepository = Mockito.mock(HabitRepository.class);
  private final HabitTrackRepository habitTrackRepository =
      Mockito.mock(HabitTrackRepository.class);
  private final UserService userService = Mockito.mock(UserService.class);
  private final CurrentUserProvider currentUserProvider = new MockCurrentUserProviderImpl();
  private final CategoryRepository categoryRepository = Mockito.mock(CategoryRepository.class);
  private HabitService habitService;
  private User sampleUser;
  private Clock fixedClock;

  @BeforeEach
  void setUp() {
    fixedClock = Clock.fixed(Instant.now(), ZoneId.systemDefault());
    sampleUser = getSampleUser(currentUserProvider.getCurrentUser()).build();
    habitService =
        new HabitServiceImpl(
            habitRepository,
            currentUserProvider,
            userService,
            categoryRepository,
            habitTrackRepository);
  }

  @Test
  void findHabitById() {}

  @Test
  void deletePositiveTestCase() {
    // given
    var sampleHabit = getSampleHabitBuilder(sampleUser.getUuid()).build();
    var expectedOutput = true;
    when(habitRepository.existsById(sampleHabit.getId())).thenReturn(expectedOutput);
    // when
    var actualOutput = habitService.delete(sampleHabit.getId());
    // then
    assertThat(actualOutput).isEqualTo(expectedOutput);
  }

  @Test
  void deleteNegativeTestCase() {
    // given
    var sampleHabit = getSampleHabitBuilder(sampleUser.getUuid()).build();
    var expectedOutput = false;
    when(habitRepository.existsById(sampleHabit.getId())).thenReturn(expectedOutput);
    // when
    var actualOutput = habitService.delete(sampleHabit.getId());
    // then
    assertThat(actualOutput).isEqualTo(expectedOutput);
  }

  @Test
  void getHabitTrackPositiveTestCase() {
    // given
    var sampleHabit = getSampleHabitBuilder(sampleUser.getUuid()).build();
    var sampleHabitTrack =
        getSampleHabitTrack(
                sampleHabit.getId(),
                ZonedDateTime.now(fixedClock).format(DateTimeFormatter.ISO_DATE_TIME))
            .build();
    var listOfTracks = List.of(sampleHabitTrack);
    when(habitTrackRepository.findByHabitUUID(sampleHabit.getId())).thenReturn(listOfTracks);
    // when
    var actualListOfTracks = habitService.getHabitTrack(sampleHabit.getId());
    // then
    assertThat(listOfTracks).isEqualTo(actualListOfTracks);
  }

  @Test
  void getHabitTracksEmptyList() {
    // given
    var sampleHabit = getSampleHabitBuilder(sampleUser.getUuid()).build();
    when(habitTrackRepository.findByHabitUUID(sampleHabit.getId()))
        .thenReturn(new ArrayList<HabitTrack>());
    // when
    var actualListOfTracks = habitService.getHabitTrack(sampleHabit.getId());
    // then
    assertThat(actualListOfTracks.size()).isEqualTo(0);
  }

  @Test
  void getAllUserHabitsPositiveTestCase() {
    // given
    var sampleHabit = getSampleHabitBuilder(sampleUser.getUuid()).build();
    when(habitRepository.findByUserUUID(sampleUser.getUuid())).thenReturn(List.of(sampleHabit));
    var sampleHabitResponse = getSampleHabitResponseBuilder(sampleUser.getUuid()).build();
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
    when(habitRepository.findByUserUUID(sampleHabit.getUserUUID())).thenReturn(new ArrayList<>());
    // when
    var actualResponsePage = habitService.getAllUserHabits();
    // then
    assertThat(actualResponsePage.getItems().size()).isEqualTo(0);
  }

  @Test
  void updateHabit() {
    // given
    var sampleHabit = getSampleHabitBuilder(sampleUser.getUuid()).build();
    var sampleUpdateRequest = getSampleHabitRequestUpdateBuilder().build();
    var updatedHabit = getSampleUpdatedHabitBuilder(sampleUser.getUuid()).build();
    var expectedHabitResponse = getSampleUpdatedHabitResponseBuilder().build();
    when(habitRepository.findById(sampleHabit.getId())).thenReturn(Optional.of(sampleHabit));
    when(habitRepository.save(updatedHabit)).thenReturn(updatedHabit);
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
    when(habitRepository.findById(sampleHabit.getId())).thenReturn(Optional.empty());
    when(habitRepository.save(updatedHabit)).thenReturn(updatedHabit);
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

    when(habitRepository.findById(sampleHabit.getId())).thenReturn(Optional.of(sampleHabit));
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

    when(habitRepository.findById(sampleHabit.getId())).thenReturn(Optional.empty());
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
}
