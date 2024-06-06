package com.dreamtracker.app.view.domain.model;


import com.dreamtracker.app.habit.adapters.api.HabitTrackingRequest;
import com.dreamtracker.app.view.adapters.api.StatsComponent;
import com.dreamtracker.app.view.adapters.api.StatsComponentResponse;
import com.dreamtracker.app.view.config.StatsUpdateRequester;
import org.springframework.stereotype.Component;

@Component
public class StatsComponentObservable implements StatsUpdateRequester {


    @Override
    public boolean addStatsComponent(StatsComponent statsComponent) {
        return false;
    }

    @Override
    public boolean removeStatsComponent(StatsComponent statsComponent) {
        return ;
    }

    @Override
    public StatsComponentResponse requestStatsUpdated(HabitTrackingRequest habitTrackingRequest) {
        return null;
    }
}
