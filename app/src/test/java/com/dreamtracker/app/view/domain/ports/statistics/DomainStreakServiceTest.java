package com.dreamtracker.app.view.domain.ports.statistics;

import com.dreamtracker.app.habit.domain.fixtures.HabitFixture;
import com.dreamtracker.app.user.config.CurrentUserProvider;
import com.dreamtracker.app.user.config.MockCurrentUserProviderImpl;
import com.dreamtracker.app.view.domain.model.aggregate.StreakAggregate;
import com.dreamtracker.app.view.domain.ports.StreakAggregateRepositoryPort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class DomainStreakServiceTest implements HabitFixture {


    private DomainStreakService domainStreakService;
    private final StreakAggregateRepositoryPort streakAggregateRepositoryPort = Mockito.mock(StreakAggregateRepositoryPort.class);
    private final CurrentUserProvider currentUserProvider = new MockCurrentUserProviderImpl();

    @BeforeEach
    void setUp() {
        domainStreakService = new DomainStreakService(streakAggregateRepositoryPort);
      }

    @Test
    void initializeAggregates() {
        var habit = getSampleHabitBuilder(currentUserProvider.getCurrentUser()).build();
        var streak = StreakAggregate.builder().id(null).longestStreak(0).currentStreak(0).habitUUID(UUID.fromString("8fbb366d-64bb-4e2a-8527-93085885270e")).build();
        var db = StreakAggregate.builder().habitUUID(habit.getId()).id(UUID.fromString("ccccb2ec-cf7a-4088-8109-d23d280e9379")).build();

        when(streakAggregateRepositoryPort.save(streak)).thenReturn(db);
        var test = domainStreakService.initializeAggregates(habit.getId()) ;
      }

    @Test
    void updateAggregatesAndCalculateResponse() {
      }

    @Test
    void getCalculateResponse() {
      }
}