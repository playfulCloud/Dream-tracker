package com.dreamtracker.app.view.config;

import com.dreamtracker.app.habit.adapters.api.HabitTrackingRequest;
import com.dreamtracker.app.view.adapters.api.StatsComponent;
import com.dreamtracker.app.view.adapters.api.StatsComponentResponse;

public interface StatsUpdateRequester {
   boolean addStatsComponent(StatsComponent statsComponent);
   boolean removeStatsComponent(StatsComponent statsComponent);
   StatsComponentResponse requestStatsUpdated(HabitTrackingRequest habitTrackingRequest);
}
