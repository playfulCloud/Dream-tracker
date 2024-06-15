package com.dreamtracker.app.view.domain.ports.statistics;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.*;

import com.dreamtracker.app.habit.domain.fixtures.HabitFixture;
import com.dreamtracker.app.user.config.CurrentUserProvider;
import com.dreamtracker.app.user.config.MockCurrentUserProviderImpl;
import com.dreamtracker.app.view.domain.model.aggregate.AggregatesFixtures;
import com.dreamtracker.app.view.domain.model.aggregate.BreaksAggregate;
import com.dreamtracker.app.view.domain.ports.BreaksAggregateRepositoryPort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class DomainBreaksServiceTest implements AggregatesFixtures, HabitFixture {

  private final BreaksAggregateRepositoryPort breaksAggregateRepositoryPort =
      Mockito.mock(BreaksAggregateRepositoryPort.class);
  private final CurrentUserProvider currentUserProvider = new MockCurrentUserProviderImpl();
  private DomainBreaksService domainBreaksService;

  @BeforeEach
  void setUp() {
    domainBreaksService = new DomainBreaksService(breaksAggregateRepositoryPort);
  }
  @Test
  void initializeAggregatesPositiveTestCase() {
    var habit = getSampleHabitBuilder(currentUserProvider.getCurrentUser()).build();
    var aggregate = getBreakAggregatePreSavedBuilder(habit.getId()).build();
    var aggregateSavedToDB = getBreakAggregateSavedBuilder(habit.getId()).build();
    var expectedComponentResponse = getBreakStatsComponentResponse().build();

    when(breaksAggregateRepositoryPort.save(aggregate)).thenReturn(aggregateSavedToDB);
    var actualComponentResponse = domainBreaksService.initializeAggregates(habit.getId());

    assertThat(actualComponentResponse).isEqualTo(expectedComponentResponse);
  }

  @Test
  void testInitializeAggregates() {
    var habit = getSampleHabitBuilder(currentUserProvider.getCurrentUser()).build();
    BreaksAggregate breakAggregate = BreaksAggregate.builder().habitUUID(habit.getId()).build();
    BreaksAggregate savedAggregate = BreaksAggregate.builder().habitUUID(habit.getId()).build();

    when(breaksAggregateRepositoryPort.save(breakAggregate)).thenReturn(savedAggregate);

    var response = domainBreaksService.initializeAggregates(habit.getId());
    verify(breaksAggregateRepositoryPort).save(breakAggregate);
  }

  @Test
  void updateAggregatesAndCalculateResponse() {}

  @Test
  void getCalculateResponse() {}
}
