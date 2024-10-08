package com.dreamtracker.app.service.impl;

import com.dreamtracker.app.fixtures.HabitFixture;
import com.dreamtracker.app.fixtures.HabitTrackFixture;
import com.dreamtracker.app.goal.domain.ports.DomainGoalService;
import com.dreamtracker.app.goal.domain.ports.GoalService;
import com.dreamtracker.app.habit.adapters.api.HabitTrackResponse;
import com.dreamtracker.app.habit.domain.model.ChartResponse;
import com.dreamtracker.app.habit.domain.model.Habit;
import com.dreamtracker.app.habit.domain.model.HabitTrack;
import com.dreamtracker.app.habit.domain.ports.DomainHabitTrackService;
import com.dreamtracker.app.habit.domain.ports.HabitRepositoryPort;
import com.dreamtracker.app.habit.domain.ports.HabitTrackRepositoryPort;
import com.dreamtracker.app.habit.domain.ports.HabitTrackService;
import com.dreamtracker.app.infrastructure.exception.EntityNotFoundException;
import com.dreamtracker.app.infrastructure.exception.ExceptionMessages;
import com.dreamtracker.app.infrastructure.response.Page;
import com.dreamtracker.app.infrastructure.utils.DateService;
import com.dreamtracker.app.user.config.CurrentUserProvider;
import com.dreamtracker.app.user.config.MockCurrentUserProviderImpl;
import com.dreamtracker.app.view.domain.model.aggregate.StatsAggregator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.platform.commons.logging.Logger;
import org.junit.platform.commons.logging.LoggerFactory;
import org.mockito.Mockito;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.Mockito.when;

class DomainHabitTrackServiceTest implements HabitFixture, HabitTrackFixture {

  private HabitTrackService habitTrackService;
  private final HabitTrackRepositoryPort
          habitTrackRepository =
      Mockito.mock(HabitTrackRepositoryPort.class);
  private final HabitRepositoryPort habitRepositoryPort = Mockito.mock(HabitRepositoryPort.class);
  private final CurrentUserProvider currentUserProvider = new MockCurrentUserProviderImpl();
  private final StatsAggregator statsAggregator = Mockito.mock(StatsAggregator.class);
  private final GoalService goalService = Mockito.mock(DomainGoalService.class);
  private final DateService dateService = new DateService();
  private static final Logger logger = LoggerFactory.getLogger(DomainGoalServiceTest.class);
  private Clock fixedClock;
  private Habit sampleHabit;


  @BeforeEach
  void setUp() {
    sampleHabit = getSampleHabitBuilder(currentUserProvider.getCurrentUser()).build();
    fixedClock = Clock.fixed(Instant.parse("2024-07-17T00:00:00Z"), ZoneOffset.UTC);
    habitTrackService =
        new DomainHabitTrackService(habitTrackRepository, habitRepositoryPort, statsAggregator,fixedClock,goalService,dateService,currentUserProvider);
  }

  @Test
  void getAllTracksOfHabitPositiveTestCase() {
    // given
    var dateForTracks = Instant.now(fixedClock);
    var sampleHabitTrack = getSampleHabitTrack(sampleHabit.getId(), dateForTracks).build();
    var sampleHabitTrackResponse = getSampleHabitTrackResponse(dateForTracks).build();
    var listOfTracks = List.of(sampleHabitTrack);
    var expectedPageItems = List.of(sampleHabitTrackResponse);
    var expectedPage = new Page<HabitTrackResponse>(expectedPageItems);
    when(habitRepositoryPort.findById(sampleHabit.getId())).thenReturn(Optional.of(sampleHabit));
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
    when(habitRepositoryPort.findById(sampleHabit.getId())).thenReturn(Optional.empty());
    // when
    var actualPageResponse = habitTrackService.getAllTracksOfHabit(sampleHabit.getId());
    // then
    assertThat(actualPageResponse.getItems().size()).isEqualTo(0);
  }

  @Test
  void trackTheHabitPositiveTestCase() {
    // given

    var dateForTracks = Instant.now(fixedClock);
    var sampleHabitTrack = getSampleHabitTrack(sampleHabit.getId(), dateForTracks).id(null).build();
    var expectedHabitTrackResponse = getSampleHabitTrackResponse(dateForTracks).build();
    var sampleHabitTrackRequest = getSampleHabitTrackRequest(sampleHabit.getId()).build();
    when(habitRepositoryPort.findById(sampleHabit.getId())).thenReturn(Optional.of(sampleHabit));
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
    when(habitRepositoryPort.findById(sampleHabit.getId())).thenReturn(Optional.empty());
    // when
    assertThatThrownBy(() -> habitTrackService.trackTheHabit(sampleHabitTrackRequest))
        // then
        .isInstanceOf(EntityNotFoundException.class)
        .hasMessage(ExceptionMessages.entityNotFoundExceptionMessage);
  }

  @Test
  void getChartFromHabitTracksPositiveTestCase() {
    //given
    var today = Instant.now(fixedClock);
    var tomorrow = Instant.now(fixedClock).plus(1, ChronoUnit.DAYS);
    var habit = getSampleHabitBuilder(currentUserProvider.getCurrentUser()).build();
    var track = getSampleHabitTrack(habit.getId(), today).build();
    var otherTrack = getSampleHabitTrack(habit.getId(), tomorrow).build();
    var listOfTracks = List.of(track, otherTrack);
    when(habitTrackRepository.findAllByUserUUID(currentUserProvider.getCurrentUser())).thenReturn(listOfTracks);
    var firstChart = new ChartResponse(today.atZone(ZoneId.systemDefault()).toLocalDate(), 1, 1);
    var secondChart = new ChartResponse(tomorrow.atZone(ZoneId.systemDefault()).toLocalDate(), 1, 1);
    var listOfCharts = List.of(secondChart, firstChart);
    var expectedOutput = new Page<>(listOfCharts);

    // when
    var actual = habitTrackService.getChartsFromHabitTracks();

    // then
    assertThat(expectedOutput).isEqualTo(actual);
  }
}
