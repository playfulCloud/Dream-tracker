package com.dreamtracker.app.view.config;

import com.dreamtracker.app.habit.adapters.api.HabitTrackResponse;
import com.dreamtracker.app.habit.adapters.api.HabitTrackingRequest;
import com.dreamtracker.app.habit.domain.model.Habit;
import com.dreamtracker.app.infrastructure.response.Page;
import com.dreamtracker.app.view.adapters.api.StatsComponentResponse;

import java.util.UUID;

public interface StatsAggregatorObservable {
   boolean initializeAggregates(UUID habitUUID);
   Page<StatsComponentResponse> requestStatsUpdated(UUID habitUUID, HabitTrackResponse habitTrackResponse);
   Page<StatsComponentResponse> getAggregates(UUID habitUUID);
}
