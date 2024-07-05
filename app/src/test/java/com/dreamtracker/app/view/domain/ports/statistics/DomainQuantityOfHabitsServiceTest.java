package com.dreamtracker.app.view.domain.ports.statistics;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.when;

import com.dreamtracker.app.habit.domain.fixtures.HabitFixture;
import com.dreamtracker.app.habit.domain.fixtures.HabitTrackFixture;
import com.dreamtracker.app.habit.domain.model.Habit;
import com.dreamtracker.app.infrastructure.utils.DateService;
import com.dreamtracker.app.user.config.CurrentUserProvider;
import com.dreamtracker.app.user.config.MockCurrentUserProviderImpl;
import com.dreamtracker.app.view.domain.model.aggregate.AggregatesFixtures;
import com.dreamtracker.app.view.domain.ports.QuantityOfHabitsAggregateRepositoryPort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class DomainQuantityOfHabitsServiceTest
    implements HabitFixture, AggregatesFixtures, HabitTrackFixture {

  private final QuantityOfHabitsAggregateRepositoryPort quantityOfHabitsAggregateRepositoryPort =
      Mockito.mock(QuantityOfHabitsAggregateRepositoryPort.class);
  private final CurrentUserProvider currentUserProvider = new MockCurrentUserProviderImpl();
  private DomainQuantityOfHabitsService domainQuantityOfHabitsService;
  private final DateService dateService = new DateService();
  private Habit habit;

    @BeforeEach
    void setUp() {
    domainQuantityOfHabitsService =
        new DomainQuantityOfHabitsService(quantityOfHabitsAggregateRepositoryPort);
    habit = getSampleHabitBuilder(currentUserProvider.getCurrentUser()).build();
      }

    @Test
    void initializeAggregates() {
    // given
    var aggregate = getQuantityOfHabitsAggregateBuilder(habit.getId()).id(null).build();
    var aggregateSavedToDB = getQuantityOfHabitsAggregateBuilder(habit.getId()).build();
    var expectedComponentResponse = getQuantityOfHabitsComponentResponse().build();

    when(quantityOfHabitsAggregateRepositoryPort.save(aggregate)).thenReturn(aggregateSavedToDB);
    // when
    var actualComponentResponse = domainQuantityOfHabitsService.initializeAggregates(habit.getId());
    // then
    assertThat(actualComponentResponse).isEqualTo(expectedComponentResponse);
      }

    @Test
    void updateAggregatesAndCalculateResponse() {

      }

    @Test
    void getCalculateResponse() {
      }
}