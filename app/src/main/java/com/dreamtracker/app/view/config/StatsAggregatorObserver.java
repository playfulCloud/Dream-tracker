package com.dreamtracker.app.view.config;

import com.dreamtracker.app.habit.adapters.api.HabitTrackingRequest;
import com.dreamtracker.app.habit.domain.model.Habit;
import com.dreamtracker.app.view.adapters.api.StatsComponentResponse;

import java.util.UUID;

public interface StatsAggregatorObserver {
    StatsComponentResponse updateAggregate(UUID habitUUID, HabitTrackingRequest habitTrackingRequest);
    StatsComponentResponse initializeAggregate(UUID habitUUID);
    StatsComponentResponse getAggregate(UUID habitUUID);
}
