package com.dreamtracker.app.view.domain.model.aggregateManagers;

import com.dreamtracker.app.habit.adapters.api.HabitTrackingRequest;
import com.dreamtracker.app.view.adapters.api.StatsComponentResponse;
import com.dreamtracker.app.view.config.StatsAggregatorObserver;
import com.dreamtracker.app.view.domain.ports.StreakAggregateRepositoryPort;
import com.dreamtracker.app.view.domain.ports.statistics.DomainStreakService;
import lombok.RequiredArgsConstructor;

import java.util.UUID;


@RequiredArgsConstructor
public class StreakAggregateManager implements StatsAggregatorObserver {

    private final DomainStreakService domainStreakService;

    @Override
    public StatsComponentResponse updateAggregate(UUID habitUUID, HabitTrackingRequest habitTrackingRequest) {
        return null;
    }

    @Override
    public StatsComponentResponse initializeAggregate(UUID habitUUID) {
        return domainStreakService.initializeAggregates(habitUUID);
    }

    @Override
    public StatsComponentResponse getAggregate(UUID habitUUID) {
        return domainStreakService.getCalculateResponse(habitUUID);
    }


}
