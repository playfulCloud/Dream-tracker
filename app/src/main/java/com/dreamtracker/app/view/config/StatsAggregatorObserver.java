package com.dreamtracker.app.view.config;

import com.dreamtracker.app.habit.adapters.api.HabitTrackResponse;
import com.dreamtracker.app.view.adapters.api.StatsComponentResponse;
import java.util.UUID;

public interface StatsAggregatorObserver {
    StatsComponentResponse updateAggregate(UUID habitUUID, HabitTrackResponse habitTrackResponse);
    StatsComponentResponse initializeAggregate(UUID habitUUID);
    StatsComponentResponse getAggregate(UUID habitUUID);
}
