package com.dreamtracker.app.view.config;

import com.dreamtracker.app.view.domain.model.aggregateManagers.*;
import com.dreamtracker.app.view.domain.ports.*;

import java.time.Clock;
import java.util.List;

import com.dreamtracker.app.view.domain.ports.statistics.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AggregateManagerProvider {

  @Bean
  QuantityOfHabitsAggregateManager getQuantityOfHabitsAggregateUpdater(
          DomainQuantityOfHabitsService domainQuantityOfHabitsService) {
    return new QuantityOfHabitsAggregateManager(domainQuantityOfHabitsService);
  }

  @Bean
  SingleDayAggregateManager getSingleDayAggregateUpdater(
      DomainSingleDayService domainSingleDayService) {
    return new SingleDayAggregateManager(domainSingleDayService);
  }

  @Bean
  DependingOnDayAggregateManager getDependingOnDayAggregateUpdater(
      DomainDependingOnDayService domainDependingOnDayService) {
    return new DependingOnDayAggregateManager(domainDependingOnDayService);
  }

  @Bean
  StreakAggregateManager getStreakAggregateUpdater(
      DomainStreakService domainStreakService) {
    return new StreakAggregateManager(domainStreakService);
  }

  @Bean
  BreakAggregateManager getBreakAggregateUpdater(
      DomainBreaksService domainBreaksService) {
    return new BreakAggregateManager(domainBreaksService);
  }

  @Bean
  public List<StatsAggregatorObserver> getAllObservers(
      DomainBreaksService domainBreaksService,
      DomainStreakService domainStreakService,
      DomainDependingOnDayService domainDependingOnDayService,
      DomainSingleDayService domainSingleDayService,
      DomainQuantityOfHabitsService domainQuantityOfHabitsService) {
    return List.of(
        getBreakAggregateUpdater(domainBreaksService),
        getStreakAggregateUpdater(domainStreakService),
        getDependingOnDayAggregateUpdater(domainDependingOnDayService),
        getSingleDayAggregateUpdater(domainSingleDayService),
        getQuantityOfHabitsAggregateUpdater(domainQuantityOfHabitsService));
  }
}
