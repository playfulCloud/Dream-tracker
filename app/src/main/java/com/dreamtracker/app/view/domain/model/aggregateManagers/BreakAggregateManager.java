package com.dreamtracker.app.view.domain.model.aggregateManagers;

import com.dreamtracker.app.habit.adapters.api.HabitTrackingRequest;
import com.dreamtracker.app.view.adapters.api.StatsComponentResponse;
import com.dreamtracker.app.view.config.StatsAggregatorObserver;
import com.dreamtracker.app.view.domain.ports.BreaksAggregateRepositoryPort;
import com.dreamtracker.app.view.domain.ports.statistics.DomainBreaksService;
import lombok.RequiredArgsConstructor;

import java.util.UUID;

@RequiredArgsConstructor
public class BreakAggregateManager implements StatsAggregatorObserver {

    private final DomainBreaksService domainBreaksService;

    @Override
    public StatsComponentResponse updateAggregate(UUID habitUUID, HabitTrackingRequest habitTrackingRequest) {
        return null;
    }

    @Override
    public StatsComponentResponse initializeAggregate(UUID habitUUID) {
        return domainBreaksService.initializeAggregates(habitUUID);
    }

    @Override
    public StatsComponentResponse getAggregate(UUID habitUUID) {
        return null;
    }
}
