package com.dreamtracker.app.view.domain.model.aggregateManagers;

import com.dreamtracker.app.habit.adapters.api.HabitTrackingRequest;
import com.dreamtracker.app.habit.domain.model.Habit;
import com.dreamtracker.app.view.adapters.api.StatsComponentResponse;
import com.dreamtracker.app.view.config.StatsAggregatorObserver;
import com.dreamtracker.app.view.domain.ports.DependingOnDayRepositoryPort;
import com.dreamtracker.app.view.domain.ports.statistics.DomainDependingOnDayService;
import lombok.RequiredArgsConstructor;

import java.util.UUID;


@RequiredArgsConstructor
public class DependingOnDayAggregateManager implements StatsAggregatorObserver {

   private final DomainDependingOnDayService domainDependingOnDayService;


    @Override
    public StatsComponentResponse updateAggregate(UUID habitUUID, HabitTrackingRequest habitTrackingRequest) {
        return null;
    }

    @Override
    public StatsComponentResponse initializeAggregate(UUID habitUUID) {
        return domainDependingOnDayService.initializeAggregates(habitUUID);
    }

    @Override
    public StatsComponentResponse getAggregate(UUID habitUUID) {
        return null;
    }
}
