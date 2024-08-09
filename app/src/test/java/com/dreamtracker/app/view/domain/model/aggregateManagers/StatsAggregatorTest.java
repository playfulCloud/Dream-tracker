package com.dreamtracker.app.view.domain.model.aggregateManagers;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.when;

import com.dreamtracker.app.fixtures.HabitFixture;
import com.dreamtracker.app.fixtures.HabitTrackFixture;
import com.dreamtracker.app.habit.domain.model.Habit;
import com.dreamtracker.app.habit.domain.model.HabitTrackStatus;
import com.dreamtracker.app.infrastructure.utils.DateService;
import com.dreamtracker.app.user.config.CurrentUserProvider;
import com.dreamtracker.app.user.config.MockCurrentUserProviderImpl;
import com.dreamtracker.app.view.adapters.api.CombinedComponentResponse;
import com.dreamtracker.app.view.config.StatsAggregatorObserver;
import com.dreamtracker.app.view.domain.model.aggregate.AggregatesFixtures;
import com.dreamtracker.app.view.domain.model.aggregate.StatsAggregator;
import com.dreamtracker.app.view.domain.ports.statistics.*;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class StatsAggregatorTest implements HabitFixture, HabitTrackFixture, AggregatesFixtures {

    private final DomainBreaksService domainBreaksService = Mockito.mock(DomainBreaksService.class);
    private final List<StatsAggregatorObserver> observers = List.of(domainBreaksService);
  private final StatsAggregator statsAggregator = new StatsAggregator(observers);
  private final CurrentUserProvider currentUserProvider = new MockCurrentUserProviderImpl();
  private final Habit habit = getSampleHabitBuilder(currentUserProvider.getCurrentFromSecurityContext()).build();
  private final DateService dateService = new DateService();

    @Test
    void initializeAggregates() {
        var breakComponentResponse = getBreakStatsComponentResponse().build();
        when(domainBreaksService.initializeAggregate(habit.getId())).thenReturn(breakComponentResponse);
        var actual = statsAggregator.initializeAggregates(habit.getId());
        assertThat(actual).isEqualTo(true);
    }

    @Test
    void requestStatsUpdated() {
        var habitTrackResponse = getSampleHabitTrackResponse(dateService.getCurrentDateInISO8601()).status(HabitTrackStatus.DONE.toString()).build();
        var breakComponentResponse = getBreakStatsComponentResponse().build();
        when(domainBreaksService.updateAggregate(habit.getId(),habitTrackResponse)).thenReturn(breakComponentResponse);
        var actual = statsAggregator.requestStatsUpdated(habit.getId(), habitTrackResponse);

    CombinedComponentResponse expected = new CombinedComponentResponse();
    expected.setAverageBreak(breakComponentResponse.getAverageBreak());
    assertThat(actual).isEqualTo(expected);
    }

    @Test
    void getAggregates() {
        var breakComponentResponse = getBreakStatsComponentResponse().build();
        when(domainBreaksService.getAggregate(habit.getId())).thenReturn(breakComponentResponse);
        var actual = statsAggregator.getAggregates(habit.getId());
    var expected = new CombinedComponentResponse();
    assertThat(actual).isEqualTo(expected);
    }
}