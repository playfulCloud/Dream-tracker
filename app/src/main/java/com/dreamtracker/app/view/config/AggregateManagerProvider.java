package com.dreamtracker.app.view.config;

import com.dreamtracker.app.view.domain.model.aggregateManagers.*;
import com.dreamtracker.app.view.domain.ports.*;
import java.util.List;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AggregateManagerProvider {

  @Bean
  QuantityOfHabitsAggregateManager getQuantityOfHabitsAggregateUpdater(
      QuantityOfHabitsAggregateRepositoryPort quantityOfHabitsAggregateRepositoryPort) {
    return new QuantityOfHabitsAggregateManager(quantityOfHabitsAggregateRepositoryPort);
  }

  @Bean
  SingleDayAggregateManager getSingleDayAggregateUpdater(
      SingleDayAggregateRepositoryPort singleDayAggregateRepositoryPort) {
    return new SingleDayAggregateManager(singleDayAggregateRepositoryPort);
  }

  @Bean
  DependingOnDayAggregateManager getDependingOnDayAggregateUpdater(
      DependingOnDayRepositoryPort dependingOnDayRepositoryPort) {
    return new DependingOnDayAggregateManager(dependingOnDayRepositoryPort);
  }

  @Bean
  StreakAggregateManager getStreakAggregateUpdater(
      StreakAggregateRepositoryPort streakAggregateRepositoryPort) {
    return new StreakAggregateManager(streakAggregateRepositoryPort);
  }

  @Bean
  BreakAggregateManager getBreakAggregateUpdater(
      BreaksAggregateRepositoryPort breaksAggregateRepositoryPort) {
    return new BreakAggregateManager(breaksAggregateRepositoryPort);
  }

  @Bean
  public List<StatsAggregatorObserver> getAllObservers(
      BreaksAggregateRepositoryPort breaksAggregateRepositoryPort,
      StreakAggregateRepositoryPort streakAggregateRepositoryPort,
      DependingOnDayRepositoryPort dependingOnDayRepositoryPort,
      SingleDayAggregateRepositoryPort singleDayAggregateRepositoryPort,
      QuantityOfHabitsAggregateRepositoryPort quantityOfHabitsAggregateRepositoryPort) {
    return List.of(
        getBreakAggregateUpdater(breaksAggregateRepositoryPort),
        getStreakAggregateUpdater(streakAggregateRepositoryPort),
        getDependingOnDayAggregateUpdater(dependingOnDayRepositoryPort),
        getSingleDayAggregateUpdater(singleDayAggregateRepositoryPort),
        getQuantityOfHabitsAggregateUpdater(quantityOfHabitsAggregateRepositoryPort));
  }
}
