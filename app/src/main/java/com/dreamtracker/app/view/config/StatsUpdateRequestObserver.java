package com.dreamtracker.app.view.config;

import com.dreamtracker.app.habit.adapters.api.HabitTrackingRequest;

public interface StatsUpdateRequestObserver {
    boolean updateStatsComponent(HabitTrackingRequest habitTrackingRequest);
}
