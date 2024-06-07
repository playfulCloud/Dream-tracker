package com.dreamtracker.app.view.domain.model.aggregateManagers;

import com.dreamtracker.app.habit.adapters.api.HabitTrackingRequest;
import com.dreamtracker.app.view.config.StatsAggregatorObserver;
import com.dreamtracker.app.view.domain.ports.StreakAggregateRepositoryPort;
import lombok.RequiredArgsConstructor;

import java.util.UUID;


@RequiredArgsConstructor
public class StreakAggregateManager implements StatsAggregatorObserver {

    private final StreakAggregateRepositoryPort streakAggregateRepositoryPort;

    @Override
    public boolean updateAggregate(UUID habitUUID, HabitTrackingRequest habitTrackingRequest) {
        return false;
    }

    @Override
    public boolean initializeAggregate(UUID habitUUID) {

        return ;
    }

    @Override
    public boolean getAggregate(UUID habitUUID) {
        return false;
    }


}
