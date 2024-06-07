package com.dreamtracker.app.view.config;

import com.dreamtracker.app.habit.adapters.api.HabitTrackingRequest;
import com.dreamtracker.app.habit.domain.model.Habit;
import com.dreamtracker.app.view.adapters.api.StatsComponentResponse;

import java.util.UUID;

public interface StatsAggregatorObservable {
   boolean initializeAggregates(UUID habitUUID);
   StatsComponentResponse requestStatsUpdated(Habit habit, HabitTrackingRequest habitTrackingRequest);
   boolean getAggregates(UUID habitUUID);
}
