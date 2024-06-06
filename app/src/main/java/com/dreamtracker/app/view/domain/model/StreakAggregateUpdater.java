package com.dreamtracker.app.view.domain.model;

import com.dreamtracker.app.habit.adapters.api.HabitTrackingRequest;
import com.dreamtracker.app.view.config.StatsUpdateRequestObserver;

public class StreakAggregateUpdater implements StatsUpdateRequestObserver {
    @Override
    public boolean updateStatsComponent(HabitTrackingRequest habitTrackingRequest) {
        return false;
    }
}
