package com.dreamtracker.app.view.domain.ports.statistics;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.dreamtracker.app.habit.domain.fixtures.HabitFixture;
import com.dreamtracker.app.user.config.CurrentUserProvider;
import com.dreamtracker.app.user.config.MockCurrentUserProviderImpl;
import com.dreamtracker.app.view.domain.model.aggregate.AggregatesFixtures;
import com.dreamtracker.app.view.domain.model.aggregate.BreaksAggregate;
import com.dreamtracker.app.view.domain.ports.BreaksAggregateRepositoryPort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.UUID;

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
  void initializeAggregates() {
    UUID habitUUID = UUID.randomUUID();

    BreaksAggregate breakAggregateSavedToDB = BreaksAggregate.builder()
            .id(UUID.fromString("ccccb2ec-cf7a-4088-8109-d23d280e9379"))
            .habitUUID(habitUUID)
            .build();
    when(breaksAggregateRepositoryPort.save(BreaksAggregate.builder().habitUUID(habitUUID).build())).thenReturn(breakAggregateSavedToDB);

    var initResponse = domainBreaksService.initializeAggregates(habitUUID);

  }

  @Test
  void updateAggregatesAndCalculateResponse() {}

  @Test
  void getCalculateResponse() {}
}
