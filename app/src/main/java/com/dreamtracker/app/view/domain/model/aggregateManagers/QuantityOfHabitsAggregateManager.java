package com.dreamtracker.app.view.domain.model.aggregateManagers;

import com.dreamtracker.app.habit.adapters.api.HabitTrackingRequest;
import com.dreamtracker.app.view.adapters.api.StatsComponentResponse;
import com.dreamtracker.app.view.config.StatsAggregatorObserver;
import com.dreamtracker.app.view.domain.ports.QuantityOfHabitsAggregateRepositoryPort;
import com.dreamtracker.app.view.domain.ports.statistics.DomainQuantityOfHabitsService;
import lombok.RequiredArgsConstructor;

import java.util.UUID;


@RequiredArgsConstructor
public class QuantityOfHabitsAggregateManager implements StatsAggregatorObserver {

    private final DomainQuantityOfHabitsService quantityOfHabitsService;


    @Override
    public StatsComponentResponse updateAggregate(UUID habitUUID, HabitTrackingRequest habitTrackingRequest) {
        return null;
    }

    @Override
    public StatsComponentResponse initializeAggregate(UUID habitUUID) {
        return quantityOfHabitsService.initializeAggregates(habitUUID);
    }

    @Override
    public StatsComponentResponse getAggregate(UUID habitUUID) {
        return quantityOfHabitsService.getCalculateResponse(habitUUID);
    }
}
