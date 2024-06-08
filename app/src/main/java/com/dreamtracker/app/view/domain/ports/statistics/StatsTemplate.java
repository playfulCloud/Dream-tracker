package com.dreamtracker.app.view.domain.ports.statistics;

import com.dreamtracker.app.habit.adapters.api.HabitTrackingRequest;
import com.dreamtracker.app.view.adapters.api.StatsComponentResponse;

import java.util.UUID;

public interface StatsTemplate {
    StatsComponentResponse initializeAggregates(UUID habitId);
    StatsComponentResponse updateAggregatesAndCalculateResponse(UUID habitId, HabitTrackingRequest habitTrackingRequest);
    StatsComponentResponse getCalculateResponse(UUID habitId);
}
