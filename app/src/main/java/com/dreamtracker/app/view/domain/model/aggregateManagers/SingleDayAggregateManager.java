package com.dreamtracker.app.view.domain.model.aggregateManagers;

import com.dreamtracker.app.habit.adapters.api.HabitTrackingRequest;
import com.dreamtracker.app.view.adapters.api.StatsComponentResponse;
import com.dreamtracker.app.view.config.StatsAggregatorObserver;
import com.dreamtracker.app.view.domain.ports.SingleDayAggregateRepositoryPort;
import com.dreamtracker.app.view.domain.ports.statistics.DomainSingleDayService;
import lombok.RequiredArgsConstructor;

import java.util.UUID;


@RequiredArgsConstructor
public class SingleDayAggregateManager implements StatsAggregatorObserver {

   private final DomainSingleDayService domainSingleDayService;

    @Override
    public StatsComponentResponse updateAggregate(UUID habitUUID, HabitTrackingRequest habitTrackingRequest) {
        return null;
    }

    @Override
    public StatsComponentResponse initializeAggregate(UUID habitUUID) {
        return domainSingleDayService.initializeAggregates(habitUUID);
    }

    @Override
    public StatsComponentResponse getAggregate(UUID habitUUID) {
    return domainSingleDayService.getCalculateResponse(habitUUID);
    }
}
