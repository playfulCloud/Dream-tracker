package com.dreamtracker.app.view.config;

import com.dreamtracker.app.habit.adapters.api.HabitTrackingRequest;
import com.dreamtracker.app.habit.domain.model.Habit;

import java.util.UUID;

public interface StatsAggregatorObserver {
    boolean updateAggregate(UUID habitUUID, HabitTrackingRequest habitTrackingRequest);
    boolean initializeAggregate(UUID habitUUID);
    boolean getAggregate(UUID habitUUID);
}
