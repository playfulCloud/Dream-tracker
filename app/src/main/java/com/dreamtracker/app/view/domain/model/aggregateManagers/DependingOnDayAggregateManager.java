package com.dreamtracker.app.view.domain.model.aggregateManagers;

import com.dreamtracker.app.habit.adapters.api.HabitTrackingRequest;
import com.dreamtracker.app.habit.domain.model.Habit;
import com.dreamtracker.app.view.config.StatsAggregatorObserver;
import com.dreamtracker.app.view.domain.ports.DependingOnDayRepositoryPort;
import lombok.RequiredArgsConstructor;

import java.util.UUID;


@RequiredArgsConstructor
public class DependingOnDayAggregateManager implements StatsAggregatorObserver {

   private final DependingOnDayRepositoryPort dependingOnDayRepositoryPort;


    @Override
    public boolean updateAggregate(UUID habitUUID, HabitTrackingRequest habitTrackingRequest) {
        return false;
    }

    @Override
    public boolean initializeAggregate(UUID habitUUID) {
        return false;
    }

    @Override
    public boolean getAggregate(UUID habitUUID) {
        return false;
    }
}
