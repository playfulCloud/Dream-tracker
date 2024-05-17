package com.dreamtracker.app.service.impl;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.Mockito.when;

import com.dreamtracker.app.habit.domain.Habit;
import com.dreamtracker.app.entity.HabitTrack;
import com.dreamtracker.app.exception.EntityNotFoundException;
import com.dreamtracker.app.exception.ExceptionMessages;
import com.dreamtracker.app.fixtures.HabitFixture;
import com.dreamtracker.app.fixtures.HabitTrackFixture;
import com.dreamtracker.app.repository.HabitRepository;
import com.dreamtracker.app.repository.HabitTrackRepository;
import com.dreamtracker.app.response.HabitTrackResponse;
import com.dreamtracker.app.response.Page;
import com.dreamtracker.app.security.CurrentUserProvider;
import com.dreamtracker.app.service.HabitTrackService;
import java.time.Clock;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class HabitTrackServiceImplTest implements HabitFixture, HabitTrackFixture {

  private HabitTrackService habitTrackService;
  private final HabitTrackRepository habitTrackRepository =
      Mockito.mock(HabitTrackRepository.class);
  private final HabitRepository habitRepository = Mockito.mock(HabitRepository.class);
  private final CurrentUserProvider currentUserProvider = new MockCurrentUserProviderImpl();
  private Clock fixedClock;
  private Habit sampleHabit;

  @BeforeEach
  void setUp() {
    sampleHabit = getSampleHabitBuilder(currentUserProvider.getCurrentUser()).build();
    fixedClock = Clock.fixed(Instant.now(), ZoneId.systemDefault());
    habitTrackService =
        new HabitTrackServiceImpl(habitTrackRepository, habitRepository, fixedClock);
  }

  @Test
  void getAllTracksOfHabitPositiveTestCase() {
    // given
    var dateForTracks = ZonedDateTime.now(fixedClock).format(DateTimeFormatter.ISO_DATE_TIME);
    var sampleHabitTrack = getSampleHabitTrack(sampleHabit.getId(), dateForTracks).build();
    var sampleHabitTrackResponse = getSampleHabitTrackResponse(dateForTracks).build();
    var listOfTracks = List.of(sampleHabitTrack);
    var expectedPageItems = List.of(sampleHabitTrackResponse);
    var expectedPage = new Page<HabitTrackResponse>(expectedPageItems);
    when(habitRepository.findById(sampleHabit.getId())).thenReturn(Optional.of(sampleHabit));
    when(habitTrackRepository.findByHabitUUID(sampleHabit.getId())).thenReturn(listOfTracks);
    // when
    var actualPageResponse = habitTrackService.getAllTracksOfHabit(sampleHabit.getId());
    // then
    assertThat(actualPageResponse).isEqualTo(expectedPage);
  }

  @Test
  void getAllTracksOfHabitEmptyPage() {
    // given
    var sampleHabit = getSampleHabitBuilder(currentUserProvider.getCurrentUser()).build();
    when(habitRepository.findById(sampleHabit.getId())).thenReturn(Optional.empty());
    // when
    var actualPageResponse = habitTrackService.getAllTracksOfHabit(sampleHabit.getId());
    // then
    assertThat(actualPageResponse.getItems().size()).isEqualTo(0);
  }

  @Test
  void trackTheHabitPositiveTestCase() {
    // given
    var dateForTracks = ZonedDateTime.now(fixedClock).format(DateTimeFormatter.ISO_DATE_TIME);
    var sampleHabitTrack = getSampleHabitTrack(sampleHabit.getId(), dateForTracks).build();
    var expectedHabitTrackResponse = getSampleHabitTrackResponse(dateForTracks).build();
    var sampleHabitTrackRequest = getSampleHabitTrackRequest(sampleHabit.getId()).build();
    when(habitRepository.findById(sampleHabit.getId())).thenReturn(Optional.of(sampleHabit));
    when(habitTrackRepository.save(
            HabitTrack.builder()
                .habitUUID(sampleHabit.getId())
                .date(dateForTracks)
                .status(sampleHabitTrackRequest.status())
                .build()))
        .thenReturn(sampleHabitTrack);
    // when
    var actualHabitTrackResponse = habitTrackService.trackTheHabit(sampleHabitTrackRequest);
    // then
    assertThat(actualHabitTrackResponse).isEqualTo(expectedHabitTrackResponse);
  }

  @Test
  void trackTheHabitEntityNotFoundException() {
    // given
    var sampleHabitTrackRequest = getSampleHabitTrackRequest(sampleHabit.getId()).build();
    when(habitRepository.findById(sampleHabit.getId())).thenReturn(Optional.empty());
    // when
    assertThatThrownBy(() -> habitTrackService.trackTheHabit(sampleHabitTrackRequest))
        // then
        .isInstanceOf(EntityNotFoundException.class)
        .hasMessage(ExceptionMessages.entityNotFoundExceptionMessage);
  }
}
