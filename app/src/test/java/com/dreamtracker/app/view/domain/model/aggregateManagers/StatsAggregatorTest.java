package com.dreamtracker.app.view.domain.model.aggregateManagers;

import com.dreamtracker.app.fixtures.HabitFixture;
import com.dreamtracker.app.fixtures.HabitTrackFixture;
import com.dreamtracker.app.habit.domain.model.Habit;
import com.dreamtracker.app.habit.domain.model.HabitTrackStatus;
import com.dreamtracker.app.infrastructure.response.Page;
import com.dreamtracker.app.infrastructure.utils.DateService;
import com.dreamtracker.app.user.config.CurrentUserProvider;
import com.dreamtracker.app.user.config.MockCurrentUserProviderImpl;
import com.dreamtracker.app.view.adapters.api.StatsComponentResponse;
import com.dreamtracker.app.view.config.StatsAggregatorObserver;
import com.dreamtracker.app.view.domain.model.aggregate.AggregatesFixtures;
import com.dreamtracker.app.view.domain.ports.statistics.*;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.when;

class StatsAggregatorTest implements HabitFixture, HabitTrackFixture, AggregatesFixtures {



    private final DomainBreaksService domainBreaksService = Mockito.mock(DomainBreaksService.class);
    private final DomainSingleDayService domainSingleDayService = Mockito.mock(DomainSingleDayService.class);
    private final DomainStreakService domainStreakService = Mockito.mock(DomainStreakService.class);
    private final DomainQuantityOfHabitsService domainQuantityOfHabitsService = Mockito.mock(DomainQuantityOfHabitsService.class);
    private final DomainDependingOnDayService domainDependingOnDayService = Mockito.mock(DomainDependingOnDayService.class);
    private final BreakAggregateManager breakAggregateManager = new BreakAggregateManager(domainBreaksService);
  private final SingleDayAggregateManager singleDayAggregateManager =
      new SingleDayAggregateManager(domainSingleDayService);
  private final QuantityOfHabitsAggregateManager quantityOfHabitsAggregateManager =
      new QuantityOfHabitsAggregateManager(domainQuantityOfHabitsService);
  private final DependingOnDayAggregateManager dependingOnDayAggregateManager =
      new DependingOnDayAggregateManager(domainDependingOnDayService);
    private final List<StatsAggregatorObserver> observers = List.of(breakAggregateManager);
  private final StatsAggregator statsAggregator = new StatsAggregator(observers);
  private final CurrentUserProvider currentUserProvider = new MockCurrentUserProviderImpl();
  private final Habit habit = getSampleHabitBuilder(currentUserProvider.getCurrentUser()).build();
  private final DateService dateService = new DateService();
  
    @Test
    void initializeAggregates() {
        var breakComponentResponse = getBreakStatsComponentResponse().build();
        when(domainBreaksService.initializeAggregates(habit.getId())).thenReturn(breakComponentResponse);
        var actual = statsAggregator.initializeAggregates(habit.getId());
        assertThat(actual).isEqualTo(true);
    }

    @Test
    void requestStatsUpdated() {
        var habitTrackResponse = getSampleHabitTrackResponse(dateService.getCurrentDateInISO8601()).status(HabitTrackStatus.DONE.toString()).build();
        var breakComponentResponse = getBreakStatsComponentResponse().build();
        when(domainBreaksService.updateAggregatesAndCalculateResponse(habit.getId(),habitTrackResponse)).thenReturn(breakComponentResponse);
        var actual = statsAggregator.requestStatsUpdated(habit.getId(), habitTrackResponse);

        List<StatsComponentResponse> responses = List.of(breakComponentResponse);
        var expectedPageOfResponses = new Page<>(responses);
        assertThat(actual).isEqualTo(expectedPageOfResponses);
    }

    @Test
    void getAggregates() {
        var breakComponentResponse = getBreakStatsComponentResponse().build();
        when(domainBreaksService.getCalculateResponse(habit.getId())).thenReturn(breakComponentResponse);
        var actual = statsAggregator.getAggregates(habit.getId());
        List<StatsComponentResponse> responses = List.of(breakComponentResponse);
        var expectedPageOfResponses = new Page<>(responses);
        assertThat(actual).isEqualTo(expectedPageOfResponses);
    }
}